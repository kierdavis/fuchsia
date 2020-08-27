package com.kierdavis.fuchsia.ui.fragment

import android.content.Context
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.kierdavis.fuchsia.database.AppDatabase
import com.kierdavis.fuchsia.model.Item
import com.kierdavis.fuchsia.ui.component.AllItemsComponent
import com.kierdavis.fuchsia.ui.component.ItemCardComponent
import kotlinx.coroutines.launch

class ItemsFragment : ComponentFragment<AllItemsComponent>(), ItemCardComponent.OnClickedListener,
    AllItemsComponent.OnAddButtonClickedListener {

    private val viewModel by viewModels<Model> {
        Model.Factory(requireContext())
    }

    override fun onCreateComponent(): AllItemsComponent =
        AllItemsComponent(requireContext(), viewLifecycleOwner).apply {
            onItemCardClickedListener = this@ItemsFragment
            onAddButtonClickedListener = this@ItemsFragment
        }

    override fun onItemCardClicked(id: Long) {
        findNavController().navigate(ItemsFragmentDirections.actionItemsToEditItem(id))
    }

    override fun onAddItemButtonClicked() {
        viewModel.addItem().observe(this) { itemId ->
            findNavController().navigate(ItemsFragmentDirections.actionItemsToEditItem(itemId))
        }
    }


    private class Model(context: Context) : ViewModel() {
        private val database = AppDatabase.getInstance(context)

        fun addItem(): LiveData<Long> {
            val liveItemId = MutableLiveData<Long>()
            viewModelScope.launch {
                liveItemId.postValue(database.itemDao().insert(Item()))
            }
            return liveItemId
        }


        class Factory(private val context: Context) : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return Model(context) as T
            }
        }
    }
}