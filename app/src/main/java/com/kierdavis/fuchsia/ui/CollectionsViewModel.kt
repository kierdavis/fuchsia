package com.kierdavis.fuchsia.ui

import android.content.Context
import androidx.lifecycle.*
import com.kierdavis.fuchsia.database.AppDatabase
import com.kierdavis.fuchsia.model.Collection
import kotlinx.coroutines.launch

class CollectionsViewModel(private val context: Context) : ViewModel() {
    val collections: LiveData<List<Collection>> = AppDatabase.getInstance(context).collectionDao().all()

    fun createCollection(): LiveData<Collection> {
        val liveCollection = MutableLiveData<Collection>()
        viewModelScope.launch {
            val collection = Collection()
            AppDatabase.getInstance(context).collectionDao().insert(collection)
            liveCollection.postValue(collection)
        }
        return liveCollection
    }

    class Factory(
        private val context: Context
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CollectionsViewModel(context) as T
        }
    }
}