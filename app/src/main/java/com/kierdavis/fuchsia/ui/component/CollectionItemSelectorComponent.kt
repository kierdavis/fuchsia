package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kierdavis.fuchsia.R
import kotlin.math.roundToInt

class CollectionItemSelectorComponent(context: Context, lifecycleOwner: LifecycleOwner) :
    Component(context, lifecycleOwner), ItemCardComponent.OnClickedListener {

    // Properties
    var onSelectedListener: OnSelectedListener? = null

    // View
    private val items = ItemCardsComponent(context, lifecycleOwner, database.itemDao().allIds()).apply {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        onCardClickedListener = this@CollectionItemSelectorComponent
    }
    private val sheet = FrameLayout(context).apply {
        id = View.generateViewId()
        addView(items.view, FrameLayout.LayoutParams(
            MATCH_PARENT,
            MATCH_PARENT
        ))
        ViewCompat.setElevation(this, context.resources.getDimension(R.dimen.collection_item_selector_sheet_elevation))
    }
    private val sheetBehavior = BottomSheetBehavior<View>().apply {
        isHideable = true
        skipCollapsed = true
        state = BottomSheetBehavior.STATE_HIDDEN
    }
    private val sheetHeight = context.resources.getDimension(R.dimen.collection_item_selector_sheet_height).roundToInt()
    private val toggleButton = FloatingActionButtonComponent(context, lifecycleOwner).apply {
        setImageResource(android.R.drawable.ic_input_add)
        setOnClickListener(View.OnClickListener {
            sheetBehavior.apply {
                state = if (state == BottomSheetBehavior.STATE_HIDDEN) {
                    BottomSheetBehavior.STATE_EXPANDED
                } else {
                    BottomSheetBehavior.STATE_HIDDEN
                }
            }
        })
    }
    override val view: View
        get() = sheet
    fun addTo(content: View): View =
        CoordinatorLayout(context).apply {
            id = View.generateViewId()
            addView(content, CoordinatorLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
            addView(sheet, CoordinatorLayout.LayoutParams(MATCH_PARENT, sheetHeight).apply {
                behavior = sheetBehavior
            })
            addView(toggleButton.view, CoordinatorLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                anchorId = sheet.id
                anchorGravity = Gravity.END or Gravity.TOP
                val margin = context.resources.getDimension(R.dimen.fab_margin).roundToInt()
                setMargins(0, 0, margin, margin)
            })
        }
    fun addTo(content: Component): View = addTo(content.view)

    override fun onItemCardClicked(id: Long) {
        onSelectedListener?.onItemSelectedForAdditionToCollection(id)
    }


    interface OnSelectedListener {
        fun onItemSelectedForAdditionToCollection(itemId: Long)
    }
}