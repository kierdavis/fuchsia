package com.kierdavis.fuchsia.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.kierdavis.fuchsia.R
import com.kierdavis.fuchsia.model.ItemWithPictures

class ItemCardRecyclerViewAdapter(private val clickListener: ItemCardClickListener) : RecyclerView.Adapter<ItemCardViewHolder>(),
    Observer<List<ItemWithPictures>> {
    private var data: List<ItemWithPictures> = emptyList()

    override fun onChanged(newData: List<ItemWithPictures>?) {
        data = newData ?: emptyList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCardViewHolder {
        return ItemCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false), clickListener)
    }

    override fun onBindViewHolder(holder: ItemCardViewHolder, position: Int) {
        holder.bind(data[position])
    }
}