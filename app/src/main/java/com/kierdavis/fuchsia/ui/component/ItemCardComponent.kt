package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.kierdavis.fuchsia.model.ItemPicture

class ItemCardComponent(context: Context, lifecycleOwner: LifecycleOwner) : Component(context, lifecycleOwner) {
    constructor(parent: Component) : this(parent.context, parent.lifecycleOwner)

    // View
    private val imageView = ImageView(context).apply {
        adjustViewBounds = true
    }
    private val cardView = CardView(context).apply {
        radius = 15F
        cardElevation = 3F
        addView(imageView, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
    }
    override val view
        get() = cardView

    // Data logic
    private val pictureObserver = Observer<ItemPicture?> { picture ->
        picture?.let { pictureDrawableCache.get(it.mediaUri) }.let { imageView.setImageDrawable(it) }
    }
    private var pictureObservable: LiveData<ItemPicture?>? = null
    var itemId: Long = 0L
        set(newItemId) {
            pictureObservable?.removeObserver(pictureObserver)
            field = newItemId
            pictureObservable = database.itemPictureDao().firstForItem(newItemId).apply {
                observe(lifecycleOwner, pictureObserver)
            }
        }

    // Interaction
    fun setOnClickListener(listener: ClickListener) {
        cardView.setOnClickListener { listener.onItemCardClicked(itemId) }
    }


    class ViewHolder(val component: ItemCardComponent) : RecyclerView.ViewHolder(component.view)


    interface ClickListener {
        fun onItemCardClicked(id: Long)
    }
}