package com.kierdavis.fuchsia.ui

import android.content.Context
import androidx.lifecycle.*
import com.kierdavis.fuchsia.model.Item
import com.kierdavis.fuchsia.model.ItemWithPictures
import com.kierdavis.fuchsia.database.AppDatabase
import kotlinx.coroutines.launch

class ItemsViewModel(private val context: Context) : ViewModel() {
    val items: LiveData<List<Item>> = AppDatabase.getInstance(context).itemDao().all()

    val itemsWithPictures: LiveData<List<ItemWithPictures>> = AppDatabase.getInstance(context).let { db ->
        Transformations.map(items) { items ->
            items.map { item ->
                ItemWithPictures(
                    item,
                    db.itemPictureDao().allForItem(item.id)
                )
            }
        }
    }

    fun createItem(): LiveData<Item> {
        val liveItem = MutableLiveData<Item>()
        viewModelScope.launch {
            val item = Item()
            AppDatabase.getInstance(context).itemDao().save(item)
            liveItem.postValue(item)
        }
        return liveItem
    }

    class Factory(
        private val context: Context
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ItemsViewModel(context) as T
        }
    }
}