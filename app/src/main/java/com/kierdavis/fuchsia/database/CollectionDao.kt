package com.kierdavis.fuchsia.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kierdavis.fuchsia.model.Collection

@Dao
abstract class CollectionDao {
    @Insert
    protected abstract suspend fun insert(collection: Collection): Long

    @Update
    protected abstract suspend fun update(collection: Collection)

    suspend fun save(collection: Collection) {
        if (collection.hasId()) {
            update(collection)
        }
        else {
            collection.id = insert(collection)
        }
    }

    @Query("SELECT * FROM Collection")
    abstract fun all(): LiveData<List<Collection>>

    @Query("SELECT * FROM Collection WHERE id = :id")
    abstract suspend fun byId(id: Long): Collection
}