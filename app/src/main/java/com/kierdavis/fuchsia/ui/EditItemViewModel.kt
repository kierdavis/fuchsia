package com.kierdavis.fuchsia.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import com.kierdavis.fuchsia.model.Item
import com.kierdavis.fuchsia.model.ItemPicture
import com.kierdavis.fuchsia.database.AppDatabase
import kotlinx.coroutines.launch

class EditItemViewModel(private val context: Context, private val itemId: Long) : ViewModel() {
    val item = MutableLiveData<Item>()

    init {
        viewModelScope.launch {
            item.postValue(AppDatabase.getInstance(context).itemDao().byId(itemId))
        }
    }

    val pictures: LiveData<List<ItemPicture>> = AppDatabase.getInstance(context).itemPictureDao().allForItem(itemId)
    val pictureMediaUris: LiveData<List<Uri>>
        get() = Transformations.map(pictures) { pics -> pics.map { it.mediaUri } }

    fun save() {
        val itemSnapshot = item.value!!
        viewModelScope.launch {
            AppDatabase.getInstance(context).itemDao().save(itemSnapshot)
        }
    }

    fun addPicture(mediaUri: Uri) {
        val itemPicture = ItemPicture(itemId, mediaUri)
        viewModelScope.launch {
            AppDatabase.getInstance(context).itemPictureDao().save(itemPicture)
        }
    }

    class Factory(
        private val context: Context,
        private val itemId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            return EditItemViewModel(context, itemId) as T
        }
    }
}
