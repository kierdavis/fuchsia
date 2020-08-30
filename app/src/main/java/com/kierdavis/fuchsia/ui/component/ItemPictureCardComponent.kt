package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import com.kierdavis.fuchsia.R
import com.kierdavis.fuchsia.model.ItemPicture
import kotlin.math.roundToInt

class ItemPictureCardComponent(context: Context, lifecycleOwner: LifecycleOwner, livePicture: LiveData<ItemPicture>?) :
    Component(context, lifecycleOwner) {

    // Properties
    var onAddButtonClickedListener
        get() = if (contents is AddButtonContents) { contents.onAddButtonClickedListener } else { null }
        set(it) { if (contents is AddButtonContents) { contents.onAddButtonClickedListener = it } }

    // View
    private val contents: Component = if (livePicture != null) {
        PictureContents(context, lifecycleOwner, livePicture)
    } else {
        AddButtonContents(context, lifecycleOwner)
    }
    private val cardView = CardView(context).apply {
        radius = 10F
        addView(contents.view, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
    }
    override val view: View
        get() = cardView


    private class PictureContents(context: Context, lifecycleOwner: LifecycleOwner, livePicture: LiveData<ItemPicture>) :
        Component(context, lifecycleOwner) {

        // View
        private val imageView = ImageView(context).apply {
            livePicture.observe(lifecycleOwner) {
                setImageDrawable(drawableCache.get(it.mediaUri))
            }
        }
        private val layout = ConstraintLayout(context).apply {
            addView(imageView, ConstraintLayout.LayoutParams(MATCH_CONSTRAINT, MATCH_CONSTRAINT).apply {
                topToTop = PARENT_ID
                startToStart = PARENT_ID
                endToEnd = PARENT_ID
                dimensionRatio = "1:1"
            })
        }
        override val view: View
            get() = layout
    }


    private class AddButtonContents(context: Context, lifecycleOwner: LifecycleOwner) :
        Component(context, lifecycleOwner) {

        // Properties
        var onAddButtonClickedListener: OnAddButtonClickedListener? = null

        // View
        private val imageView = ImageView(context).apply {
            setImageResource(android.R.drawable.ic_input_add)
            setOnClickListener { onAddButtonClickedListener?.onAddItemPictureButtonClicked() }
        }
        private val layout = ConstraintLayout(context).apply {
            addView(imageView, ConstraintLayout.LayoutParams(MATCH_CONSTRAINT, MATCH_CONSTRAINT).apply {
                topToTop = PARENT_ID
                startToStart = PARENT_ID
                endToEnd = PARENT_ID
                dimensionRatio = "1:1"
                val margin = context.resources.getDimension(R.dimen.add_item_picture_button_margin).roundToInt()
                setMargins(margin, margin, margin, margin)
            })
        }
        override val view: View
            get() = layout
    }


    interface OnAddButtonClickedListener {
        fun onAddItemPictureButtonClicked()
    }
}