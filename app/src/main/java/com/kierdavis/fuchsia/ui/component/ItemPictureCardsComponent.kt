package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.kierdavis.fuchsia.model.ItemPicture

class ItemPictureCardsComponent(context: Context, lifecycleOwner: LifecycleOwner, val livePictures: LiveData<List<ItemPicture>>) :
    Component(context, lifecycleOwner) {

    // Data
    val pictures
        get() = livePictures.value ?: emptyList()

    // Properties
    var onAddButtonClickedListener: ItemPictureCardComponent.OnAddButtonClickedListener? = null

    // View
    private val recyclerView = RecyclerView(context).apply {
        adapter = object : RecyclerView.Adapter<ViewHolder>() {
            override fun getItemCount(): Int = pictures.size + 1
            override fun getItemViewType(position: Int): Int =
                if (position < pictures.size) {
                    ViewType.PICTURE.ordinal
                } else {
                    ViewType.BUTTON.ordinal
                }
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val livePicture = when (viewType) {
                    ViewType.PICTURE.ordinal -> MutableLiveData<ItemPicture>()
                    else -> null
                }
                val component = ItemPictureCardComponent(context, lifecycleOwner, livePicture)
                component.onAddButtonClickedListener = onAddButtonClickedListenerProxy
                return ViewHolder(component.view, livePicture)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.livePicture?.apply { value = pictures[position] }
            }
        }
    }
    override val view: View
        get() = recyclerView

    // Glue
    private var onAddButtonClickedListenerProxy = object : ItemPictureCardComponent.OnAddButtonClickedListener {
        override fun onAddItemPictureButtonClicked() {
            onAddButtonClickedListener?.onAddItemPictureButtonClicked()
        }
    }


    private enum class ViewType { PICTURE, BUTTON }
    private class ViewHolder(view: View, val livePicture: MutableLiveData<ItemPicture>?) : RecyclerView.ViewHolder(view)
}