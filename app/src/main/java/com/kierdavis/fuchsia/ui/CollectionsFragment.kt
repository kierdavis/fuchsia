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
import com.kierdavis.fuchsia.databinding.CollectionsBinding
import com.kierdavis.fuchsia.model.Collection

class CollectionsFragment : Fragment(), CollectionCardClickListener {
    private val viewModel by viewModels<CollectionsViewModel> {
        CollectionsViewModel.Factory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.collections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CollectionsBinding.bind(view).let { dataBinding ->
            dataBinding.lifecycleOwner = this
            CollectionCardRecyclerViewAdapter(this).let { recyclerViewAdapter ->
                viewModel.collections.observe(viewLifecycleOwner, recyclerViewAdapter)
                dataBinding.collectionsCards.adapter = recyclerViewAdapter
                dataBinding.collectionsCards.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                dataBinding.collectionsCards.addItemDecoration(PaddingDecoration(10))
            }
            dataBinding.collectionsAddButton.setOnClickListener { onNewCollectionClicked() }
        }
    }

    private fun onNewCollectionClicked() {
        viewModel.createCollection().observe(this) { collection ->
            findNavController().navigate(CollectionsFragmentDirections.actionCollectionsToEditCollection(collection.id))
        }
    }

    override fun onCollectionCardClicked(collection: Collection) {
        findNavController().navigate(CollectionsFragmentDirections.actionCollectionsToEditCollection(collection.id))
    }
}