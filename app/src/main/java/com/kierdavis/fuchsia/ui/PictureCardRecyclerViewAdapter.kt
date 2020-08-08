package com.kierdavis.fuchsia.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.kierdavis.fuchsia.R
import com.kierdavis.fuchsia.model.ItemPicture

class PictureCardRecyclerViewAdapter(private val newPictureClickListener: NewPictureClickListener) : RecyclerView.Adapter<PictureCardViewHolder>(),
    Observer<List<Uri>> {
    private var data: List<Uri> = emptyList()

    override fun onChanged(newData: List<Uri>?) {
        data = newData ?: emptyList()
        notifyDataSetChanged()
    }

    private fun getPictureCount(): Int {
        return data.size
    }

    override fun getItemCount(): Int {
        return getPictureCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < getPictureCount()) {
            R.id.existing_picture_view_type
        } else {
            R.id.new_picture_view_type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.id.existing_picture_view_type -> {
                ExistingPictureCardViewHolder(inflater.inflate(R.layout.existing_picture, parent, false))
            }
            R.id.new_picture_view_type -> {
                NewPictureCardViewHolder(inflater.inflate(R.layout.new_picture, parent, false), newPictureClickListener)
            }
            else -> {
                throw RuntimeException("bad viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: PictureCardViewHolder, position: Int) {
        if (holder is ExistingPictureCardViewHolder) {
            holder.bind(data[position])
        }
    }
}