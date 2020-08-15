package com.kierdavis.fuchsia.ui

import android.net.Uri
import android.view.View
import com.kierdavis.fuchsia.databinding.ExistingPictureCardBinding

class ExistingPictureCardViewHolder(view: View) : PictureCardViewHolder(view) {
    private val dataBinding: ExistingPictureCardBinding = ExistingPictureCardBinding.bind(view)

    fun bind(mediaUri: Uri) {
        dataBinding.existingPictureImage.setImageDrawable(
            PictureDrawableCache.getInstance(view.context).get(mediaUri)
        )
    }
}