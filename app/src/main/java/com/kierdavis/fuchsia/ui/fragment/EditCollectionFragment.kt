package com.kierdavis.fuchsia.ui.fragment

import android.content.Context
import android.view.MenuItem
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kierdavis.fuchsia.R
import com.kierdavis.fuchsia.database.AppDatabase
import com.kierdavis.fuchsia.model.Collection
import com.kierdavis.fuchsia.model.CollectionItem
import com.kierdavis.fuchsia.ui.component.CollectionEditorComponent
import com.kierdavis.fuchsia.ui.component.CollectionItemSelectorComponent
import com.kierdavis.fuchsia.ui.component.ItemCardComponent
import kotlinx.coroutines.launch

class EditCollectionFragment : ComponentFragment<CollectionEditorComponent>(),
    ItemCardComponent.OnClickedListener, CollectionItemSelectorComponent.OnSelectedListener {

    private val args: EditCollectionFragmentArgs by navArgs()
    private val viewModel by viewModels<Model> {
        Model.Factory(requireContext(), args.id)
    }

    override fun onCreateComponent(): CollectionEditorComponent =
        CollectionEditorComponent(requireContext(), viewLifecycleOwner, viewModel.liveCollection).apply {
            onItemCardClickedListener = this@EditCollectionFragment
            onItemSelectedForAdditionListener = this@EditCollectionFragment
        }

    override fun onItemCardClicked(id: Long) {
        findNavController().navigate(EditCollectionFragmentDirections.actionEditCollectionToEditItem(id))
    }

    override fun onItemSelectedForAdditionToCollection(itemId: Long) {
        viewModel.addItemToCollection(itemId)
    }

    override val menuRes = R.menu.editcollection
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuitem_editcollection_delete -> { onDeleteCollectionClicked(); true }
            else -> super.onOptionsItemSelected(menuItem)
        }
    private fun onDeleteCollectionClicked() {
        viewModel.deleteCollection()
        findNavController().navigateUp()
    }


    private class Model(context: Context, collectionId: Long) : ViewModel() {
        private val database = AppDatabase.getInstance(context)
        val liveCollection = MutableLiveData<Collection>()

        init {
            Transformations.distinctUntilChanged(database.collectionDao().byId(collectionId)).observeForever {
                liveCollection.value = it
            }
            Transformations.distinctUntilChanged(liveCollection).observeForever {
                viewModelScope.launch {
                    database.collectionDao().update(it)
                }
            }
        }

        fun addItemToCollection(itemId: Long) {
            viewModelScope.launch {
                liveCollection.value?.let { database.collectionItemDao().insert(CollectionItem(it.id, itemId)) }
            }
        }

        fun deleteCollection() {
            viewModelScope.launch {
                liveCollection.value?.let { database.collectionDao().delete(it) }
            }
        }


        class Factory(private val context: Context, private val collectionId: Long): ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return Model(context, collectionId) as T
            }
        }
    }
}
