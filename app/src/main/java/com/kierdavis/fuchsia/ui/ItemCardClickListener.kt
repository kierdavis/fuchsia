package com.kierdavis.fuchsia.ui

import com.kierdavis.fuchsia.model.ItemWithPictures

interface ItemCardClickListener {
    // TODO rename
    fun onItemCardClicked(itemWithPictures: ItemWithPictures)
}