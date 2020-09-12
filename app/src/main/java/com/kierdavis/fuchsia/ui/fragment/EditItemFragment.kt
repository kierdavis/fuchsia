package com.kierdavis.fuchsia.ui.fragment

import android.content.Context
import android.net.Uri
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kierdavis.fuchsia.R
import com.kierdavis.fuchsia.TemporaryMediaUriManager
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

    override fun onAddItemPictureButtonClicked() {
        AlertDialog.Builder(requireContext()).apply {
            // setMessage("Foobar:")
            setCancelable(true)
            // It doesn't make much sense to call these "positive" and "negative" buttons, but
            // AlertDialog.Builder only allows one button of each type.
            setPositiveButton("Take picture using camera") { _, _ ->
                viewModel.getMediaUriForCapture().observe(viewLifecycleOwner) { capturePicture.launch(it) }
            }
            setNegativeButton("Select picture from library") { _, _ ->
                getPicture.launch("image/*")
            }
        }.create().show()
    }
    private val capturePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        viewModel.addCapturedPicture()
    }
    private val getPicture = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { mediaUris ->
        viewModel.importPictures(mediaUris)
    }

    override val menuRes = R.menu.edititem
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.menuitem_edititem_delete -> { onDeleteItemClicked(); true }
            else -> super.onOptionsItemSelected(menuItem)
        }
    private fun onDeleteItemClicked() {
        viewModel.deleteItem()
        findNavController().navigateUp()
    }


    private class Model(private val context: Context, val itemId: Long) : ViewModel() {
        private val database
            get() = AppDatabase.getInstance(context)
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

        fun getMediaUriForCapture(): LiveData<Uri> {
            val result = MutableLiveData<Uri>()
            viewModelScope.launch {
                TemporaryMediaUriManager.prune(context)
                result.postValue(TemporaryMediaUriManager.create(context))
            }
            return result
        }

        fun addCapturedPicture() {
            viewModelScope.launch {
                val mediaUri = TemporaryMediaUriManager.makePermanent(context) ?: return@launch
                database.itemPictureDao().insert(ItemPicture(itemId = itemId, mediaUri = mediaUri))
            }
        }

        fun importPictures(externalMediaUris: List<Uri>) {
            viewModelScope.launch {
                externalMediaUris.forEach { externalMediaUri ->
                    TemporaryMediaUriManager.prune(context)
                    val internalMediaUri = TemporaryMediaUriManager.create(context) ?: return@forEach
                    (context.contentResolver.openInputStream(externalMediaUri) ?: return@forEach).use { inputStream ->
                        (context.contentResolver.openOutputStream(internalMediaUri) ?: return@forEach).use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    TemporaryMediaUriManager.makePermanent(context, internalMediaUri) ?: return@forEach
                    database.itemPictureDao().insert(ItemPicture(itemId = itemId, mediaUri = internalMediaUri))
                }
            }
        }

        fun deleteItem() {
            viewModelScope.launch {
                liveItem.value?.let { database.itemDao().delete(it) }
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
