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
    protected abstract suspend fun insert(itemPicture: ItemPicture): Long

    @Update
    protected abstract suspend fun update(itemPicture: ItemPicture)

    suspend fun save(itemPicture: ItemPicture) {
        if (itemPicture.hasId()) {
            update(itemPicture)
        }
        else {
            itemPicture.id = insert(itemPicture)
        }
    }

    @Query("SELECT * FROM ItemPicture WHERE itemId = :itemId")
    abstract fun allForItem(itemId: Long): LiveData<List<ItemPicture>>
}