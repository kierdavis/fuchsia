package com.kierdavis.fuchsia.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Collection {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
    var name: String = ""

    fun hasId(): Boolean {
        return id != 0L
    }
}