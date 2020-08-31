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

class FloatingActionButtonComponent(context: Context, lifecycleOwner: LifecycleOwner) : Component(context, lifecycleOwner) {
    // Properties
    fun setImageResource(it: Int) {
        button.setImageResource(it)
    }
    fun setOnClickListener(it: View.OnClickListener) {
        button.setOnClickListener(it)
    }

    // View
    private val button = FloatingActionButton(context).apply {
        ImageViewCompat.setImageTintList(
            this,
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.secondaryTextColor))
        )
        size = FloatingActionButton.SIZE_AUTO
        isClickable = true
        isFocusable = true
    }
    override val view: View
        get() = button
    fun addTo(content: View): View =
        FrameLayout(context).apply {
            addView(content, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
            addView(button, FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                gravity = Gravity.END or Gravity.BOTTOM
                val margin = context.resources.getDimension(R.dimen.fab_margin).roundToInt()
                setMargins(0, 0, margin, margin)
            })
        }
    fun addTo(content: Component): View = addTo(content.view)
}