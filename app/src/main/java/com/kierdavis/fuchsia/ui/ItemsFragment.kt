package com.kierdavis.fuchsia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.kierdavis.fuchsia.R
import com.kierdavis.fuchsia.databinding.ItemsBinding
import com.kierdavis.fuchsia.ui.component.ItemCardComponent
import com.kierdavis.fuchsia.ui.component.ItemCardsComponent

class ItemsFragment : Fragment(), ItemCardComponent.OnClickedListener {
    private val viewModel by viewModels<ItemsViewModel> {
        ItemsViewModel.Factory(requireContext())
    }

    private lateinit var itemCards: ItemCardsComponent

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
        itemCards = ItemCardsComponent(requireContext(), viewLifecycleOwner).apply {
            onCardClickedListener = this@ItemsFragment
        }
        viewModel.itemIds.observe(viewLifecycleOwner) { itemCards.itemIds = it }
        ItemsBinding.bind(view).let { dataBinding ->
            dataBinding.lifecycleOwner = this
            dataBinding.itemsCards.addView(itemCards.view)
            dataBinding.itemsAddButton.setOnClickListener {
                onNewItemClicked()
            }
        }
    }

    private fun onNewItemClicked() {
        viewModel.createItem().observe(this) { item ->
            findNavController().navigate(ItemsFragmentDirections.actionItemsToEditItem(item.id))
        }
    }

    override fun onItemCardClicked(id: Long) {
        findNavController().navigate(ItemsFragmentDirections.actionItemsToEditItem(id))
    }
}