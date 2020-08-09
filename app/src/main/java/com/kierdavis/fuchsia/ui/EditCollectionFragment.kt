package com.kierdavis.fuchsia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.kierdavis.fuchsia.R
import com.kierdavis.fuchsia.databinding.EditCollectionBinding

class EditCollectionFragment : Fragment() {
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
        }
    }

    private fun onFieldChanged() {
        viewModel.save()
    }
}
