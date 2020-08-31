package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner

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
    private val addButtonComponent = FloatingActionButtonComponent(context, lifecycleOwner).apply {
        setImageResource(android.R.drawable.ic_input_add)
        setOnClickListener(View.OnClickListener { onAddButtonClickedListener?.onAddCollectionButtonClicked() })
    }
    override val view = addButtonComponent.addTo(collectionsComponent)


    interface OnAddButtonClickedListener {
        fun onAddCollectionButtonClicked()
    }
}