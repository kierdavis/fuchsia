package com.kierdavis.fuchsia.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TemporaryMediaUri(
    @PrimaryKey
    val uri: Uri
)