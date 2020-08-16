package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.kierdavis.fuchsia.ui.PaddingDecoration

class ItemCardsComponent(context: Context, lifecycleOwner: LifecycleOwner) : Component(context, lifecycleOwner) {
    constructor(parent: Component) : this(parent.context, parent.lifecycleOwner)

    // Properties
    var itemIds: List<Long>
        get() = adapter.itemIds
        set(x) { adapter.itemIds = x }
    var onCardClickedListener: ItemCardComponent.OnClickedListener? = null

    // View
    private val adapter = Adapter(this)
    private val recyclerView = RecyclerView(context).apply {
        adapter = this@ItemCardsComponent.adapter
        layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        addItemDecoration(PaddingDecoration(10))
    }
    override val view
        get() = recyclerView

    // Glue
    private var onCardClickedListenerProxy = object : ItemCardComponent.OnClickedListener {
        override fun onItemCardClicked(id: Long) {
            onCardClickedListener?.onItemCardClicked(id)
        }
    }


    private class Adapter(private val component: ItemCardsComponent) : RecyclerView.Adapter<ItemCardComponent.ViewHolder>() {
        var itemIds: List<Long> = emptyList()
            set(x) {
                field = x
                notifyDataSetChanged()
            }

        override fun getItemCount(): Int = itemIds.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCardComponent.ViewHolder =
            ItemCardComponent.ViewHolder(ItemCardComponent(component).apply {
                onCardClickedListener = component.onCardClickedListenerProxy
            })

        override fun onBindViewHolder(holder: ItemCardComponent.ViewHolder, position: Int) {
            holder.component.itemId = itemIds[position]
        }
    }
}