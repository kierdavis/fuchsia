package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.os.Build
import android.text.InputType
import android.view.View
import android.view.View.IMPORTANT_FOR_AUTOFILL_NO
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.observe
import com.kierdavis.fuchsia.MyTransformations
import com.kierdavis.fuchsia.R
import com.kierdavis.fuchsia.model.Collection

class CollectionEditorCoreComponent(context: Context, lifecycleOwner: LifecycleOwner, val liveCollection: MutableLiveData<Collection>) :
    Component(context, lifecycleOwner) {

    constructor(parent: Component, liveCollection: MutableLiveData<Collection>) : this(parent.context, parent.lifecycleOwner, liveCollection)

    // Properties
    var onItemCardClickedListener
        get() = itemsComponent.onCardClickedListener
        set(it) { itemsComponent.onCardClickedListener = it }

    // View
    private val nameView = EditText(context).apply {
        id = R.id.collection_editor_core_component_name_view
        hint = "Name"
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        isSingleLine = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            importantForAutofill = IMPORTANT_FOR_AUTOFILL_NO
        }
        Transformations.map(liveCollection) { it.name }.observe(lifecycleOwner) { setText(it) }
        onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                liveCollection.value = liveCollection.value?.copy(name = text.toString())
            }
        }
    }
    private val itemsComponent = ItemCardsComponent(this).apply {
        MyTransformations.fold(lifecycleOwner, Transformations.map(liveCollection) { database.itemDao().idsInCollection(it.id) })
            .observe(lifecycleOwner) { itemIds = it }
    }
    private val layout = ConstraintLayout(context).apply {
        addView(nameView, ConstraintLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            topToTop = PARENT_ID
            startToStart = PARENT_ID
            endToEnd = PARENT_ID
        })
        addView(itemsComponent.view, ConstraintLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            topToBottom = nameView.id
            startToStart = PARENT_ID
            endToEnd = PARENT_ID
        })
    }
    override val view
        get() = layout
}