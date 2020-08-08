package com.kierdavis.fuchsia.ui

import com.kierdavis.fuchsia.model.ItemWithPictures

interface ItemCardClickListener {
    fun onItemCardClick(itemWithPictures: ItemWithPictures)
}