package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.observe
import com.kierdavis.fuchsia.MyTransformations

class ItemCardComponent(context: Context, lifecycleOwner: LifecycleOwner, val liveItemId: LiveData<Long>) : Component(context, lifecycleOwner) {
    // Data
    val liveFirstPicture = MyTransformations.flatten(lifecycleOwner, Transformations.map(liveItemId) { database.itemPictureDao().firstForItem(it) })
    val itemId
        get() = liveItemId.value ?: 0L

    // Properties
    var onCardClickedListener: OnClickedListener? = null

    // View
    private val imageView = ImageView(context).apply {
        adjustViewBounds = true
        liveFirstPicture.observe(lifecycleOwner) { firstPicture ->
            setImageDrawable(firstPicture?.let { drawableCache.get(it.mediaUri) })
        }
    }
    private val cardView = CardView(context).apply {
        radius = 15F
        cardElevation = 3F
        addView(imageView, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        setOnClickListener { onCardClickedListener?.onItemCardClicked(itemId) }
    }
    override val view: View
        get() = cardView


    interface OnClickedListener {
        fun onItemCardClicked(id: Long)
    }
}