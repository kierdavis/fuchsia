package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.kierdavis.fuchsia.ui.PaddingDecoration

class ItemCardsComponent(context: Context, lifecycleOwner: LifecycleOwner, clickListener: ItemCardComponent.ClickListener? = null) : Component(context, lifecycleOwner) {
    constructor(parent: Component, clickListener: ItemCardComponent.ClickListener? = null) : this(parent.context, parent.lifecycleOwner, clickListener)

    // View
    private val adapter = Adapter(this, clickListener)
    private val recyclerView = RecyclerView(context).apply {
        adapter = this@ItemCardsComponent.adapter
        layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        addItemDecoration(PaddingDecoration(10))
    }
    override val view
        get() = recyclerView

    // Data logic
    var itemIds: List<Long>
        get() = adapter.itemIds
        set(x) { adapter.itemIds = x }


    class Adapter(private val parent: ItemCardsComponent, private val clickListener: ItemCardComponent.ClickListener?) : RecyclerView.Adapter<ItemCardComponent.ViewHolder>() {
        var itemIds: List<Long> = emptyList()
            set(x) {
                field = x
                notifyDataSetChanged()
            }

        override fun getItemCount(): Int = itemIds.size

        override fun onCreateViewHolder(parentView: ViewGroup, viewType: Int): ItemCardComponent.ViewHolder =
            ItemCardComponent.ViewHolder(ItemCardComponent(parent).apply {
                clickListener?.let { setOnClickListener(it) }
            })

        override fun onBindViewHolder(holder: ItemCardComponent.ViewHolder, position: Int) {
            holder.component.itemId = itemIds[position]
        }
    }
}