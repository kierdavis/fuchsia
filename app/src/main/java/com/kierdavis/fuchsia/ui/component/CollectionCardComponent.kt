package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.observe
import com.kierdavis.fuchsia.R
import com.kierdavis.fuchsia.model.Collection
import kotlin.math.roundToInt

class CollectionCardComponent(context: Context, lifecycleOwner: LifecycleOwner, val liveCollection: LiveData<Collection>) : Component(context, lifecycleOwner) {
    // Data
    val liveName = Transformations.map(liveCollection) { it.name }
    val collection
        get() = liveCollection.value
    val collectionId
        get() = collection?.id ?: 0L

    // Properties
    var onCardClickedListener: OnClickedListener? = null

    // View
    private val textView = TextView(context).apply {
        id = View.generateViewId()
        liveName.observe(lifecycleOwner) { text = it }
    }
    private val cardView = CardView(context).apply {
        id = View.generateViewId()
        radius = 15F
        cardElevation = 3F
        addView(textView, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            val padding = context.resources.getDimension(R.dimen.primary_card_text_padding).roundToInt()
            setPadding(padding, padding, padding, padding)
        })
        setOnClickListener { onCardClickedListener?.onCollectionCardClicked(collectionId) }
    }
    override val view: View
        get() = cardView


    interface OnClickedListener {
        fun onCollectionCardClicked(id: Long)
    }
}