package com.kierdavis.fuchsia.ui

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.kierdavis.fuchsia.ui.component.ItemCardComponent

class ItemCardRecyclerViewAdapter(private val lifecycleOwner: LifecycleOwner, private val clickListener: ItemCardComponent.ClickListener) : RecyclerView.Adapter<ItemCardComponent.Holder>(),
    Observer<List<Long>> {

    private var itemIds: List<Long> = emptyList()

    override fun onChanged(newItemIds: List<Long>?) {
        itemIds = newItemIds ?: emptyList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemIds.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCardComponent.Holder {
        return ItemCardComponent.Holder(ItemCardComponent(parent.context, lifecycleOwner).apply {
            setOnClickListener(clickListener)
        })
    }

    override fun onBindViewHolder(holder: ItemCardComponent.Holder, position: Int) {
        holder.view.itemId = itemIds[position]
    }
}