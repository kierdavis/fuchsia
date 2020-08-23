package com.kierdavis.fuchsia.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kierdavis.fuchsia.model.Item

@Dao
abstract class ItemDao {
    @Insert
    abstract suspend fun insert(item: Item): Long

    @Update
    abstract suspend fun update(item: Item)

    @Query("SELECT id FROM Item")
    abstract fun allIds(): LiveData<List<Long>>

    @Query("SELECT itemId FROM CollectionItem WHERE collectionId = :collectionId")
    abstract fun idsInCollection(collectionId: Long): LiveData<List<Long>>

    @Query("SELECT * FROM Item WHERE id = :id LIMIT 1")
    abstract fun byId(id: Long): LiveData<Item>
}