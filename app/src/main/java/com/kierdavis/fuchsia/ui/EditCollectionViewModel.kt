package com.kierdavis.fuchsia.ui

import android.content.Context
import androidx.lifecycle.*
import com.kierdavis.fuchsia.database.AppDatabase
import com.kierdavis.fuchsia.model.Collection
import com.kierdavis.fuchsia.model.ItemWithPictures
import kotlinx.coroutines.launch

class EditCollectionViewModel(private val context: Context, private val collectionId: Long) :
    ViewModel() {
    val collection = MutableLiveData<Collection>()

    init {
        viewModelScope.launch {
            collection.postValue(
                AppDatabase.getInstance(context).collectionDao().byId(collectionId)
            )
        }
    }

    val itemIds: LiveData<List<Long>> = AppDatabase.getInstance(context).run {
        itemDao().idsInCollection(collectionId)
    }

    fun save() {
        val collectionSnapshot = collection.value!!
        viewModelScope.launch {
            AppDatabase.getInstance(context).collectionDao().save(collectionSnapshot)
        }
    }

    class Factory(
        private val context: Context,
        private val collectionId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditCollectionViewModel(context, collectionId) as T
        }
    }
}
