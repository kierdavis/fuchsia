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
import com.kierdavis.fuchsia.model.Collection
import com.kierdavis.fuchsia.ui.PaddingDecoration

class CollectionCardsComponent(context: Context, lifecycleOwner: LifecycleOwner, val liveCollections: LiveData<List<Collection>>) : Component(context, lifecycleOwner) {
    // Data
    val collections
        get() = liveCollections.value ?: emptyList()

    // Properties
    var onCardClickedListener: CollectionCardComponent.OnClickedListener? = null

    // View
    private val recyclerView = RecyclerView(context).apply {
        adapter = object : RecyclerView.Adapter<ViewHolder>() {
            init { liveCollections.observe(lifecycleOwner) { notifyDataSetChanged() } }
            override fun getItemCount(): Int = collections.size
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val liveCollection = MutableLiveData<Collection>()
                val component = CollectionCardComponent(context, lifecycleOwner, liveCollection)
                component.onCardClickedListener = onCardClickedListenerProxy
                return ViewHolder(component.view, liveCollection)
            }
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.liveCollection.value = collections[position]
            }
        }
        layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        addItemDecoration(PaddingDecoration(10))
    }
    override val view: View
        get() = recyclerView

    // Glue
    private var onCardClickedListenerProxy = object : CollectionCardComponent.OnClickedListener {
        override fun onCollectionCardClicked(id: Long) {
            onCardClickedListener?.onCollectionCardClicked(id)
        }
    }


    private class ViewHolder(view: View, val liveCollection: MutableLiveData<Collection>) : RecyclerView.ViewHolder(view)
}