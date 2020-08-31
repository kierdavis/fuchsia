package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner

class AllItemsComponent(context: Context, lifecycleOwner: LifecycleOwner) :
    Component(context, lifecycleOwner) {

    // Properties
    var onItemCardClickedListener
        get() = itemsComponent.onCardClickedListener
        set(it) {
            itemsComponent.onCardClickedListener = it
        }
    var onAddButtonClickedListener: OnAddButtonClickedListener? = null

    // View
    private val itemsComponent =
        ItemCardsComponent(context, lifecycleOwner, database.itemDao().allIds())
    private val addButtonComponent = FloatingActionButtonComponent(context, lifecycleOwner).apply {
        setImageResource(android.R.drawable.ic_input_add)
        setOnClickListener(View.OnClickListener { onAddButtonClickedListener?.onAddItemButtonClicked() })
    }
    override val view = addButtonComponent.addTo(itemsComponent)


    interface OnAddButtonClickedListener {
        fun onAddItemButtonClicked()
    }
}