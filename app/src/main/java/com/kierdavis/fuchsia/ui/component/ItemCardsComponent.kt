package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.kierdavis.fuchsia.ui.PaddingDecoration

class ItemCardsComponent(context: Context, lifecycleOwner: LifecycleOwner, val liveItemIds: LiveData<List<Long>>) : Component(context, lifecycleOwner) {
    // Data
    val itemIds
        get() = liveItemIds.value ?: emptyList()

    // Properties
    var onCardClickedListener: ItemCardComponent.OnClickedListener? = null

    // View
    private val recyclerView = RecyclerView(context).apply {
        id = View.generateViewId()
        adapter = object : RecyclerView.Adapter<ViewHolder>() {
            init { liveItemIds.observe(lifecycleOwner) { notifyDataSetChanged() } }
            override fun getItemCount(): Int = itemIds.size
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val liveItemId = MutableLiveData<Long>()
                val component = ItemCardComponent(context, lifecycleOwner, liveItemId)
                component.onCardClickedListener = onCardClickedListenerProxy
                return ViewHolder(component.view, liveItemId)
            }
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.liveItemId.value = itemIds[position]
            }
        }
        layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        addItemDecoration(PaddingDecoration(10))
    }
    override val view: View
        get() = recyclerView

    // Glue
    private var onCardClickedListenerProxy = object : ItemCardComponent.OnClickedListener {
        override fun onItemCardClicked(id: Long) {
            onCardClickedListener?.onItemCardClicked(id)
        }
    }


    private class ViewHolder(view: View, val liveItemId: MutableLiveData<Long>) : RecyclerView.ViewHolder(view)
}