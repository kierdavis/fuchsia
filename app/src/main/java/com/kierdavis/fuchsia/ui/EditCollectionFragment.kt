package com.kierdavis.fuchsia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kierdavis.fuchsia.R
import com.kierdavis.fuchsia.databinding.EditCollectionBinding
import com.kierdavis.fuchsia.ui.component.ItemCardComponent

class EditCollectionFragment : Fragment(), ItemCardComponent.ClickListener {
    private val args: EditCollectionFragmentArgs by navArgs()

    private val viewModel by viewModels<EditCollectionViewModel> {
        EditCollectionViewModel.Factory(requireContext(), args.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.edit_collection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EditCollectionBinding.bind(view).let { dataBinding ->
            dataBinding.lifecycleOwner = this
            dataBinding.viewModel = viewModel
            dataBinding.editCollectionNameField.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    onFieldChanged()
                }
            }
            ItemCardRecyclerViewAdapter(viewLifecycleOwner, this).let { recyclerViewAdapter ->
                viewModel.itemIds.observe(viewLifecycleOwner, recyclerViewAdapter)
                dataBinding.editCollectionItems.adapter = recyclerViewAdapter
                dataBinding.editCollectionItems.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                dataBinding.editCollectionItems.addItemDecoration(PaddingDecoration(10))
            }
            dataBinding.editCollectionAddItemButton.setOnClickListener { onAddItemClicked() }
        }
    }

    private fun onFieldChanged() {
        viewModel.save()
    }

    private fun onAddItemClicked() {
        Snackbar.make(requireView(), "onAddItemClicked", Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemCardClicked(id: Long) {
        findNavController().navigate(EditCollectionFragmentDirections.actionEditCollectionToEditItem(id))
    }
}
