package com.kierdavis.fuchsia.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String = ""
)