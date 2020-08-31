package com.kierdavis.fuchsia.ui.fragment

import android.content.Context
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.kierdavis.fuchsia.database.AppDatabase
import com.kierdavis.fuchsia.model.Collection
import com.kierdavis.fuchsia.ui.component.AllCollectionsComponent
import com.kierdavis.fuchsia.ui.component.CollectionCardComponent
import kotlinx.coroutines.launch

class CollectionsFragment : ComponentFragment<AllCollectionsComponent>(),
    CollectionCardComponent.OnClickedListener, AllCollectionsComponent.OnAddButtonClickedListener {

    private val viewModel by viewModels<Model> {
        Model.Factory(requireContext())
    }

    override fun onCreateComponent(): AllCollectionsComponent =
        AllCollectionsComponent(requireContext(), viewLifecycleOwner).apply {
            onCollectionCardClickedListener = this@CollectionsFragment
            onAddButtonClickedListener = this@CollectionsFragment
        }

    override fun onCollectionCardClicked(id: Long) {
        findNavController().navigate(CollectionsFragmentDirections.actionCollectionsToEditCollection(id))
    }

    override fun onAddCollectionButtonClicked() {
        viewModel.addCollection().observe(this) { collectionId ->
            findNavController().navigate(CollectionsFragmentDirections.actionCollectionsToEditCollection(collectionId))
        }
    }


    private class Model(context: Context) : ViewModel() {
        private val database = AppDatabase.getInstance(context)

        fun addCollection(): LiveData<Long> {
            val liveCollectionId = MutableLiveData<Long>()
            viewModelScope.launch {
                liveCollectionId.postValue(database.collectionDao().insert(Collection()))
            }
            return liveCollectionId
        }


        class Factory(private val context: Context) : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return Model(context) as T
            }
        }
    }
}