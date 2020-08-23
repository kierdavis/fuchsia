package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner

class AllItemsComponent(context: Context, lifecycleOwner: LifecycleOwner) : Component(context, lifecycleOwner) {
    // Properties
    var onItemCardClickedListener
        get() = itemsComponent.onCardClickedListener
        set(it) { itemsComponent.onCardClickedListener = it }

    // View
    private val itemsComponent = ItemCardsComponent(context, lifecycleOwner, database.itemDao().allIds())
    override val view: View
        get() = itemsComponent.view
}