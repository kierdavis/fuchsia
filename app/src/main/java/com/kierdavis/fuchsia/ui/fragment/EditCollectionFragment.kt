package com.kierdavis.fuchsia.ui.fragment

import android.content.Context
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kierdavis.fuchsia.database.AppDatabase
import com.kierdavis.fuchsia.model.Collection
import com.kierdavis.fuchsia.ui.component.CollectionEditorComponent
import com.kierdavis.fuchsia.ui.component.ItemCardComponent
import kotlinx.coroutines.launch

class EditCollectionFragment : ComponentFragment<CollectionEditorComponent>(), ItemCardComponent.OnClickedListener {
    private val args: EditCollectionFragmentArgs by navArgs()
    private val viewModel by viewModels<Model> {
        Model.Factory(
            requireContext(),
            args.id
        )
    }

    override fun onCreateComponent(): CollectionEditorComponent =
        CollectionEditorComponent(requireContext(), viewLifecycleOwner, viewModel.liveCollection).apply {
            onItemCardClickedListener = this@EditCollectionFragment
        }

    override fun onItemCardClicked(id: Long) {
        findNavController().navigate(EditCollectionFragmentDirections.actionEditCollectionToEditItem(id))
    }


    private class Model(context: Context, collectionId: Long) : ViewModel() {
        val liveCollection = MutableLiveData<Collection>()

        init {
            val database = AppDatabase.getInstance(context)
            Transformations.distinctUntilChanged(database.collectionDao().byId(collectionId)).observeForever {
                liveCollection.value = it
            }
            Transformations.distinctUntilChanged(liveCollection).observeForever {
                viewModelScope.launch {
                    database.collectionDao().update(it)
                }
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
