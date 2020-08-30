package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.os.Build
import android.text.InputType.*
import android.view.View
import android.view.View.IMPORTANT_FOR_AUTOFILL_NO
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.observe
import com.kierdavis.fuchsia.MyTransformations
import com.kierdavis.fuchsia.model.Item

class ItemEditorComponent(context: Context, lifecycleOwner: LifecycleOwner, val liveItem: MutableLiveData<Item>) : Component(context, lifecycleOwner) {
    // Data
    val livePictures = MyTransformations.flatten(lifecycleOwner, Transformations.map(liveItem) { database.itemPictureDao().allForItem(it.id) })

    // Properties
    var onAddPictureButtonClickedListener
        get() = picturesComponent.onAddButtonClickedListener
        set(it) { picturesComponent.onAddButtonClickedListener = it }

    // View
    private val nameView = EditText(context).apply {
        id = View.generateViewId()
        hint = "Name"
        inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_SHORT_MESSAGE or TYPE_TEXT_FLAG_CAP_SENTENCES
        isSingleLine = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            importantForAutofill = IMPORTANT_FOR_AUTOFILL_NO
        }
        Transformations.map(liveItem) { it.name }.observe(lifecycleOwner) { setText(it) }
        onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                liveItem.value = liveItem.value?.copy(name = text.toString())
            }
        }
    }
    private val picturesComponent = ItemPictureCardsComponent(context, lifecycleOwner, livePictures)
    private val layout = ConstraintLayout(context).apply {
        addView(nameView, ConstraintLayout.LayoutParams(MATCH_CONSTRAINT, WRAP_CONTENT).apply {
            topToTop = PARENT_ID
            startToStart = PARENT_ID
            endToEnd = PARENT_ID
        })
        addView(picturesComponent.view, ConstraintLayout.LayoutParams(MATCH_CONSTRAINT, WRAP_CONTENT).apply {
            topToBottom = nameView.id
            startToStart = PARENT_ID
            endToEnd = PARENT_ID
        })
    }
    override val view: View
        get() = layout
}