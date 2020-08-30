package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.observe
import com.kierdavis.fuchsia.MyTransformations

class ItemCardComponent(context: Context, lifecycleOwner: LifecycleOwner, val liveItemId: LiveData<Long>) : Component(context, lifecycleOwner) {
    // Data
    val liveItem = MyTransformations.flatten(lifecycleOwner, Transformations.map(liveItemId) { database.itemDao().byId(it) })
    val liveFirstPicture = MyTransformations.flatten(lifecycleOwner, Transformations.map(liveItemId) { database.itemPictureDao().firstForItem(it) })
    val itemId
        get() = liveItemId.value ?: 0L

    // Properties
    var onCardClickedListener: OnClickedListener? = null

    // View
    private val textView = TextView(context).apply {
        liveItem.observe(lifecycleOwner) { text = it.name }
    }
    private val imageView = ImageView(context).apply {
        adjustViewBounds = true
        liveFirstPicture.observe(lifecycleOwner) { firstPicture ->
            if (firstPicture != null) {
                setImageDrawable(drawableCache.get(firstPicture.mediaUri))
                visibility = View.VISIBLE
                textView.visibility = View.GONE
            } else {
                textView.visibility = View.VISIBLE
                visibility = View.GONE
            }
        }
    }
    private val cardView = CardView(context).apply {
        radius = 15F
        cardElevation = 3F
        addView(textView, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        addView(imageView, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        setOnClickListener { onCardClickedListener?.onItemCardClicked(itemId) }
    }
    override val view: View
        get() = cardView


    interface OnClickedListener {
        fun onItemCardClicked(id: Long)
    }
}