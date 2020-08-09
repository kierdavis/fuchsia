package com.kierdavis.fuchsia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kierdavis.fuchsia.R
import com.kierdavis.fuchsia.databinding.ItemsBinding
import com.kierdavis.fuchsia.model.ItemWithPictures

class ItemsFragment : Fragment(), ItemCardClickListener {
    private val viewModel by viewModels<ItemsViewModel> {
        ItemsViewModel.Factory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ItemsBinding.bind(view).let { dataBinding ->
            dataBinding.lifecycleOwner = this
            ItemCardRecyclerViewAdapter(this).let { recyclerViewAdapter ->
                viewModel.itemsWithPictures.observe(viewLifecycleOwner, recyclerViewAdapter)
                dataBinding.itemsCards.adapter = recyclerViewAdapter
                dataBinding.itemsCards.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                dataBinding.itemsCards.addItemDecoration(PaddingDecoration(10))
            }
            dataBinding.itemsAddButton.setOnClickListener {
                onNewItemClick()
            }
        }
    }

    // TODO rename
    private fun onNewItemClick() {
        viewModel.createItem().observe(this) { item ->
            findNavController().navigate(ItemsFragmentDirections.actionItemsToEditItem(item.id))
        }
    }

    override fun onItemCardClick(itemWithPictures: ItemWithPictures) {
        findNavController().navigate(ItemsFragmentDirections.actionItemsToEditItem(itemWithPictures.item.id))
    }
}