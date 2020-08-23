package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner

class AllCollectionsComponent(context: Context, lifecycleOwner: LifecycleOwner) : Component(context, lifecycleOwner) {
    // Data
    val liveCollections = database.collectionDao().all()

    // Properties
    var onCollectionCardClickedListener
        get() = collectionsComponent.onCardClickedListener
        set(it) { collectionsComponent.onCardClickedListener = it }

    private val collectionsComponent = CollectionCardsComponent(context, lifecycleOwner, liveCollections)
    override val view: View
        get() = collectionsComponent.view
}