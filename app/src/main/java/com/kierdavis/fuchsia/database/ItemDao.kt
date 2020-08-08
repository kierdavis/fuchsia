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
    protected abstract suspend fun insert(item: Item): Long

    @Update
    protected abstract suspend fun update(item: Item)

    suspend fun save(item: Item) {
        if (item.hasId()) {
            update(item)
        }
        else {
            item.id = insert(item)
        }
    }

    @Query("SELECT * FROM Item")
    abstract fun all(): LiveData<List<Item>>

    @Query("SELECT * FROM Item WHERE id = :id")
    abstract suspend fun byId(id: Long): Item
}