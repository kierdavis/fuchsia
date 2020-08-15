package com.kierdavis.fuchsia.ui

import android.view.View
import com.kierdavis.fuchsia.databinding.NewPictureBinding

class NewPictureCardViewHolder(view: View, listener: NewPictureClickListener) : PictureCardViewHolder(view) {
    private val dataBinding: NewPictureBinding = NewPictureBinding.bind(view)

    init {
        dataBinding.newPictureCard.setOnClickListener { listener.onNewPictureClicked() }
    }
}