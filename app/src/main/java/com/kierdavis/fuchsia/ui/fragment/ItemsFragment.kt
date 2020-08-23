package com.kierdavis.fuchsia.ui.fragment

import androidx.navigation.fragment.findNavController
import com.kierdavis.fuchsia.ui.component.AllItemsComponent
import com.kierdavis.fuchsia.ui.component.ItemCardComponent

class ItemsFragment : ComponentFragment<AllItemsComponent>(), ItemCardComponent.OnClickedListener {
    override fun onCreateComponent(): AllItemsComponent =
        AllItemsComponent(requireContext(), viewLifecycleOwner).apply {
            onItemCardClickedListener = this@ItemsFragment
        }

    /*
    private fun onNewItemClicked() {
        viewModel.createItem().observe(this) { item ->
            findNavController().navigate(ItemsFragmentDirections.actionItemsToEditItem(item.id))
        }
    }
     */

    override fun onItemCardClicked(id: Long) {
        findNavController().navigate(
            ItemsFragmentDirections.actionItemsToEditItem(
                id
            )
        )
    }
}