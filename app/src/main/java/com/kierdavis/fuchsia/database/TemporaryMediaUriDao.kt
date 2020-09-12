package com.kierdavis.fuchsia.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kierdavis.fuchsia.model.TemporaryMediaUri

@Dao
abstract class TemporaryMediaUriDao {
    @Insert
    abstract suspend fun insert(entity: TemporaryMediaUri)

    @Delete
    abstract suspend fun delete(entity: TemporaryMediaUri)

    @Delete
    abstract suspend fun delete(entities: List<TemporaryMediaUri>)

    @Query("SELECT * FROM TemporaryMediaUri LIMIT 1")
    abstract suspend fun get(): TemporaryMediaUri?

    @Query("SELECT * FROM TemporaryMediaUri")
    abstract suspend fun getAll(): List<TemporaryMediaUri>
}