package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.kierdavis.fuchsia.model.Collection

class CollectionEditorComponent(context: Context, lifecycleOwner: LifecycleOwner, liveCollection: MutableLiveData<Collection>) :
    Component(context, lifecycleOwner) {

    // Properties
    var onItemCardClickedListener
        get() = core.onItemCardClickedListener
        set(it) { core.onItemCardClickedListener = it }
    var onItemSelectedForAdditionListener
        get() = selector.onSelectedListener
        set(it) { selector.onSelectedListener = it }

    // View
    private val core = CollectionEditorCoreComponent(context, lifecycleOwner, liveCollection)
    private val selector = CollectionItemSelectorComponent(context, lifecycleOwner)
    override val view: View = selector.addTo(core)
}