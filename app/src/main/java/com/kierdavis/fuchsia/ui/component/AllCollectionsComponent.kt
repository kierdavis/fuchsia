package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kierdavis.fuchsia.R
import kotlin.math.roundToInt

class AllCollectionsComponent(context: Context, lifecycleOwner: LifecycleOwner) :
    Component(context, lifecycleOwner) {

    // Properties
    var onCollectionCardClickedListener
        get() = collectionsComponent.onCardClickedListener
        set(it) {
            collectionsComponent.onCardClickedListener = it
        }
    var onAddButtonClickedListener: OnAddButtonClickedListener? = null

    private val collectionsComponent =
        CollectionCardsComponent(context, lifecycleOwner, database.collectionDao().all())
    private val addButton = FloatingActionButton(context).apply {
        setImageResource(android.R.drawable.ic_input_add)
        ImageViewCompat.setImageTintList(
            this,
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.secondaryTextColor))
        )
        size = FloatingActionButton.SIZE_AUTO
        isClickable = true
        isFocusable = true
        setOnClickListener { onAddButtonClickedListener?.onAddCollectionButtonClicked() }
    }
    private val layout = FrameLayout(context).apply {
        addView(collectionsComponent.view, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        addView(addButton, FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            gravity = Gravity.END or Gravity.BOTTOM
            val margin = context.resources.getDimension(R.dimen.fab_margin).roundToInt()
            setMargins(0, 0, margin, margin)
        })
    }
    override val view: View
        get() = layout


    interface OnAddButtonClickedListener {
        fun onAddCollectionButtonClicked()
    }
}