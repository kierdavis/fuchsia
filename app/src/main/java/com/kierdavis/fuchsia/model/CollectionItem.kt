package com.kierdavis.fuchsia.model

import androidx.room.Entity

@Entity(primaryKeys = ["collectionId", "itemId"])
data class CollectionItem(
    val collectionId: Long,
    val itemId: Long
)