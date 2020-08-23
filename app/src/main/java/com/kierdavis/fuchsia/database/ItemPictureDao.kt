package com.kierdavis.fuchsia.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kierdavis.fuchsia.model.ItemPicture

@Dao
abstract class ItemPictureDao {
    @Insert
    abstract suspend fun insert(itemPicture: ItemPicture): Long

    @Update
    abstract suspend fun update(itemPicture: ItemPicture)

    @Query("SELECT * FROM ItemPicture WHERE itemId = :itemId")
    abstract fun allForItem(itemId: Long): LiveData<List<ItemPicture>>

    @Query("SELECT * FROM ItemPicture WHERE itemId = :itemId ORDER BY id LIMIT 1")
    abstract fun firstForItem(itemId: Long): LiveData<ItemPicture?>
}