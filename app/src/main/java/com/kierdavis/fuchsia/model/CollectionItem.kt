package com.kierdavis.fuchsia.model

import androidx.room.Entity

@Entity(primaryKeys = ["collectionId", "itemId"])
class CollectionItem {
    var collectionId: Long = 0L
    var itemId: Long = 0L
}