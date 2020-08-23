package com.kierdavis.fuchsia.ui.fragment

import android.content.Context
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.navArgs
import com.kierdavis.fuchsia.ui.PictureCapture
import com.kierdavis.fuchsia.database.AppDatabase
import com.kierdavis.fuchsia.model.Item
import com.kierdavis.fuchsia.model.ItemPicture
import com.kierdavis.fuchsia.ui.component.ItemEditorComponent
import com.kierdavis.fuchsia.ui.component.ItemPictureCardComponent
import kotlinx.coroutines.launch

class EditItemFragment : ComponentFragment<ItemEditorComponent>(), ItemPictureCardComponent.OnAddButtonClickedListener {
    private val args: EditItemFragmentArgs by navArgs()
    private val viewModel by viewModels<Model> {
        Model.Factory(requireContext(), args.id)
    }

    override fun onCreateComponent(): ItemEditorComponent =
        ItemEditorComponent(requireContext(), viewLifecycleOwner, viewModel.liveItem).apply {
            onAddPictureButtonClickedListener = this@EditItemFragment
        }

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { onAddItemPictureResult() }
    override fun onAddItemPictureButtonClicked() {
        // TODO support importing existing photo from media library
        PictureCapture.launch(
            requireActivity(),
            viewModel.liveItem.value?.name ?: "untitled-item",
            takePicture
        )
    }
    private fun onAddItemPictureResult() {
        PictureCapture.finish(requireActivity())?.let { mediaUri -> viewModel.addPicture(mediaUri) }
    }


    private class Model(context: Context, val itemId: Long) : ViewModel() {
        private val database = AppDatabase.getInstance(context)
        val liveItem = MutableLiveData<Item>()

        init {
            Transformations.distinctUntilChanged(database.itemDao().byId(itemId)).observeForever {
                liveItem.value = it
            }
            Transformations.distinctUntilChanged(liveItem).observeForever {
                viewModelScope.launch {
                    database.itemDao().update(it)
                }
            }
        }

        fun addPicture(mediaUri: Uri) {
            val picture = ItemPicture(itemId = itemId, mediaUri = mediaUri)
            viewModelScope.launch {
                database.itemPictureDao().insert(picture)
            }
        }


        class Factory(private val context: Context, private val itemId: Long) : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return Model(context, itemId) as T
            }
        }
    }
}
