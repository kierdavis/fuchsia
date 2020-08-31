package com.kierdavis.fuchsia.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kierdavis.fuchsia.model.Item

@Dao
abstract class ItemDao {
    @Insert
    abstract suspend fun insert(item: Item): Long

    @Update
    abstract suspend fun update(item: Item)

    @Delete
    abstract suspend fun delete(item: Item)

    @Query("SELECT id FROM Item")
    abstract fun allIds(): LiveData<List<Long>>

    @Query("SELECT * FROM Item WHERE id = :id LIMIT 1")
    abstract fun byId(id: Long): LiveData<Item>
}