package com.kierdavis.fuchsia.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kierdavis.fuchsia.model.Item
import com.kierdavis.fuchsia.model.ItemPicture
import com.kierdavis.fuchsia.model.ItemWithPictures

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

    fun augmentItems(items: LiveData<List<Item>>): LiveData<List<ItemWithPictures>> =
        Transformations.map(items) { items ->
            items.map { item ->
                ItemWithPictures(item, allForItem(item.id))
            }
        }

    @Query("SELECT * FROM ItemPicture WHERE itemId = :itemId ORDER BY id LIMIT 1")
    abstract fun firstForItem(itemId: Long): LiveData<ItemPicture?>
}