package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.kierdavis.fuchsia.database.AppDatabase
import com.kierdavis.fuchsia.model.ItemPicture
import com.kierdavis.fuchsia.ui.PictureDrawableCache

class ItemCardComponent(context: Context, private val lifecycleOwner: LifecycleOwner) : CardView(context), Observer<ItemPicture?> {
    // UI
    private val imageView = ImageView(context).apply {
        adjustViewBounds = true
    }
    init {
        radius = 15F
        cardElevation = 3F
        addView(imageView, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
    }

    // Data
    private var realItemId: Long = 0L
    private var boundObservable: LiveData<ItemPicture?>? = null
    var itemId
        get() = realItemId
        set(newItemId) {
            boundObservable?.removeObserver(this)
            realItemId = newItemId
            boundObservable = AppDatabase.getInstance(context).itemPictureDao().firstForItem(itemId).apply {
                observe(lifecycleOwner, this@ItemCardComponent)
            }
        }
    override fun onChanged(picture: ItemPicture?) {
        picture?.let { PictureDrawableCache.getInstance(context).get(it.mediaUri) }.let { imageView.setImageDrawable(it) }
    }

    // Interaction
    fun setOnClickListener(listener: ClickListener) {
        super.setOnClickListener { listener.onItemCardClicked(itemId) }
    }


    class Holder(val view: ItemCardComponent) : RecyclerView.ViewHolder(view)

    interface ClickListener {
        fun onItemCardClicked(id: Long)
    }
}