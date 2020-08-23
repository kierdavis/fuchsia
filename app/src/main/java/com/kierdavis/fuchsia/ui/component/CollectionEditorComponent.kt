package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.kierdavis.fuchsia.model.Collection

class CollectionEditorComponent(context: Context, lifecycleOwner: LifecycleOwner, liveCollection: MutableLiveData<Collection>) :
    Component(context, lifecycleOwner) {

    // Properties
    var onItemCardClickedListener
        get() = coreComponent.onItemCardClickedListener
        set(it) { coreComponent.onItemCardClickedListener = it }

    // View
    private val coreComponent = CollectionEditorCoreComponent(context, lifecycleOwner, liveCollection)
    private val layout = CoordinatorLayout(context).apply {
        addView(coreComponent.view, CoordinatorLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
    }
    override val view: View
        get() = layout
}