package com.kierdavis.fuchsia.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kierdavis.fuchsia.model.Collection

@Dao
abstract class CollectionDao {
    @Insert
    abstract suspend fun insert(collection: Collection): Long

    @Update
    abstract suspend fun update(collection: Collection)

    @Delete
    abstract suspend fun delete(collection: Collection)

    @Query("SELECT * FROM Collection")
    abstract fun all(): LiveData<List<Collection>>

    @Query("SELECT * FROM Collection WHERE id = :id LIMIT 1")
    abstract fun byId(id: Long): LiveData<Collection>
}