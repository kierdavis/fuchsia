package com.kierdavis.fuchsia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.kierdavis.fuchsia.PictureCapture
import com.kierdavis.fuchsia.R
import com.kierdavis.fuchsia.databinding.EditItemBinding

class EditItemFragment : Fragment(), NewPictureClickListener {
    private val args: EditItemFragmentArgs by navArgs()

    private val viewModel by viewModels<EditItemViewModel> {
        EditItemViewModel.Factory(requireContext(), args.id)
    }

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { onNewPictureResult() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    this@EditItemFragment.onBackPressed()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.edit_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EditItemBinding.bind(view).let { dataBinding ->
            dataBinding.lifecycleOwner = this
            dataBinding.viewModel = viewModel
            dataBinding.editItemNameField.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    onFieldChanged()
                }
            }
            PictureCardRecyclerViewAdapter(this).let { recyclerViewAdapter ->
                viewModel.pictureMediaUris.observe(this, recyclerViewAdapter)
                dataBinding.editItemPictures.adapter = recyclerViewAdapter
                dataBinding.editItemPictures.layoutManager = GridLayoutManager(requireContext(), 3)
                dataBinding.editItemPictures.addItemDecoration(PaddingDecoration(5))
            }
        }
    }

    private fun onFieldChanged() {
        viewModel.save()
    }

    override fun onNewPictureClick() {
        PictureCapture.launch(
            requireActivity(),
            viewModel.item.value?.name ?: "untitled-item",
            takePicture
        )
    }

    private fun onNewPictureResult() {
        PictureCapture.finish(requireActivity())?.let { mediaUri -> viewModel.addPicture(mediaUri) }
    }

    private fun onBackPressed() {
        viewModel.save()
        findNavController().popBackStack()
    }
}
