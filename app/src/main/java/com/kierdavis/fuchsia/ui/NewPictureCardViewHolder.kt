package com.kierdavis.fuchsia.ui

import android.view.View
import com.kierdavis.fuchsia.databinding.NewPictureCardBinding

class NewPictureCardViewHolder(view: View, listener: NewPictureClickListener) : PictureCardViewHolder(view) {
    init {
        NewPictureCardBinding.bind(view).apply {
            newPictureCard.setOnClickListener { listener.onNewPictureClicked() }
        }
    }
}