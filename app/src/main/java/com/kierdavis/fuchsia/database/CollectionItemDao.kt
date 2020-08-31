package com.kierdavis.fuchsia.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kierdavis.fuchsia.model.CollectionItem

@Dao
abstract class CollectionItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(collectionItem: CollectionItem): Long

    @Query("SELECT itemId FROM CollectionItem WHERE collectionId = :collectionId")
    abstract fun itemIdsInCollection(collectionId: Long): LiveData<List<Long>>
}