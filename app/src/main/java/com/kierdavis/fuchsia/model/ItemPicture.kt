package com.kierdavis.fuchsia.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ItemPicture(
    var itemId: Long,
    var mediaUri: Uri
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    fun hasId(): Boolean {
        return id != 0L
    }
}