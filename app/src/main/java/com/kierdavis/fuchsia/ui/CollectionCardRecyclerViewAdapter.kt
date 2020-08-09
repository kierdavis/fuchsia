package com.kierdavis.fuchsia.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.kierdavis.fuchsia.R
import com.kierdavis.fuchsia.model.Collection

class CollectionCardRecyclerViewAdapter(private val clickListener: CollectionCardClickListener) : RecyclerView.Adapter<CollectionCardViewHolder>(),
    Observer<List<Collection>> {
    private var data: List<Collection> = emptyList()

    override fun onChanged(newData: List<Collection>?) {
        data = newData ?: emptyList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionCardViewHolder {
        return CollectionCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.collection_card, parent, false), clickListener)
    }

    override fun onBindViewHolder(holder: CollectionCardViewHolder, position: Int) {
        holder.bind(data[position])
    }
}