package com.kierdavis.fuchsia.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemPicture(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    val itemId: Long,
    val mediaUri: Uri
)