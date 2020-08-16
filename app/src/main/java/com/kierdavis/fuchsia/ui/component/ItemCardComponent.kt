package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.kierdavis.fuchsia.RetargetableObserver
import com.kierdavis.fuchsia.model.ItemPicture

class ItemCardComponent(context: Context, lifecycleOwner: LifecycleOwner) : Component(context, lifecycleOwner) {
    constructor(parent: Component) : this(parent.context, parent.lifecycleOwner)

    // Properties
    var itemId: Long = 0L
        set(newItemId) {
            field = newItemId
            pictureObserver.observedData = database.itemPictureDao().firstForItem(newItemId)
        }
    var onCardClickedListener: OnClickedListener? = null

    // View
    private val imageView = ImageView(context).apply {
        adjustViewBounds = true
    }
    private val cardView = CardView(context).apply {
        radius = 15F
        cardElevation = 3F
        addView(imageView, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        setOnClickListener { onCardClickedListener?.onItemCardClicked(itemId) }
    }
    override val view
        get() = cardView

    // Glue
    private val pictureObserver = object : RetargetableObserver<ItemPicture?>(lifecycleOwner) {
        override fun onChanged(picture: ItemPicture?) {
            picture?.mediaUri?.let { pictureDrawableCache.get(it) }.let { imageView.setImageDrawable(it) }
        }
    }


    class ViewHolder(val component: ItemCardComponent) : RecyclerView.ViewHolder(component.view)


    interface OnClickedListener {
        fun onItemCardClicked(id: Long)
    }
}