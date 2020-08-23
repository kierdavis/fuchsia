package com.kierdavis.fuchsia.ui.fragment

import androidx.navigation.fragment.findNavController
import com.kierdavis.fuchsia.ui.component.AllCollectionsComponent
import com.kierdavis.fuchsia.ui.component.CollectionCardComponent

class CollectionsFragment : ComponentFragment<AllCollectionsComponent>(), CollectionCardComponent.OnClickedListener {
    override fun onCreateComponent(): AllCollectionsComponent =
        AllCollectionsComponent(requireContext(), viewLifecycleOwner).apply {
            onCollectionCardClickedListener = this@CollectionsFragment
        }

    /*
    private fun onNewCollectionClicked() {
        viewModel.createCollection().observe(this) { collection ->
            findNavController().navigate(CollectionsFragmentDirections.actionCollectionsToEditCollection(collection.id))
        }
    }
     */

    override fun onCollectionCardClicked(id: Long) {
        findNavController().navigate(CollectionsFragmentDirections.actionCollectionsToEditCollection(id))
    }
}