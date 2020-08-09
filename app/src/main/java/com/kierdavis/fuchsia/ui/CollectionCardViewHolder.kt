package com.kierdavis.fuchsia.ui

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.kierdavis.fuchsia.databinding.CollectionCardBinding
import com.kierdavis.fuchsia.databinding.ItemBinding
import com.kierdavis.fuchsia.model.ItemPicture
import com.kierdavis.fuchsia.model.ItemWithPictures
import com.kierdavis.fuchsia.model.Collection

class CollectionCardViewHolder(val view: View, private val listener: CollectionCardClickListener) : RecyclerView.ViewHolder(view) {
    private val dataBinding: CollectionCardBinding = CollectionCardBinding.bind(view)

    fun bind(collection: Collection) {
        dataBinding.collection = collection
        dataBinding.collectionCard.setOnClickListener { listener.onCollectionCardClicked(collection) }
    }
}