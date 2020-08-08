package com.kierdavis.fuchsia.model

import androidx.lifecycle.LiveData

data class ItemWithPictures(val item: Item, val pictures: LiveData<List<ItemPicture>>)