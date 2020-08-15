package com.kierdavis.fuchsia.ui

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.kierdavis.fuchsia.databinding.ItemCardBinding
import com.kierdavis.fuchsia.model.ItemPicture
import com.kierdavis.fuchsia.model.ItemWithPictures

class ItemCardViewHolder(val view: View, private val listener: ItemCardClickListener) : RecyclerView.ViewHolder(view),
    Observer<List<ItemPicture>> {

    private val dataBinding: ItemCardBinding = ItemCardBinding.bind(view)

    private var currentItemWithPictures: ItemWithPictures? = null

    fun bind(itemWithPictures: ItemWithPictures) {
        currentItemWithPictures?.pictures?.removeObserver(this)
        dataBinding.itemCard.setOnClickListener { listener.onItemCardClicked(itemWithPictures) }
        dataBinding.itemCardImage.setImageDrawable(null)
        itemWithPictures.pictures.observeForever(this)
        currentItemWithPictures = itemWithPictures
    }

    override fun onChanged(pictures: List<ItemPicture>?) {
        pictures?.getOrNull(0)?.let { picture ->
            dataBinding.itemCardImage.setImageDrawable(
                PictureDrawableCache.getInstance(view.context).get(picture.mediaUri)
            )
        }
    }
}