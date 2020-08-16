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
    abstract suspend fun insert(collection: Collection): Long

    @Update
    abstract suspend fun update(collection: Collection)

    @Query("SELECT * FROM Collection")
    abstract fun all(): LiveData<List<Collection>>

    @Query("SELECT * FROM Collection WHERE id = :id LIMIT 1")
    abstract fun byId(id: Long): LiveData<Collection>
}