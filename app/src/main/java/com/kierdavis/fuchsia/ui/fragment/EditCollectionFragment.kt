package com.kierdavis.fuchsia.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kierdavis.fuchsia.database.AppDatabase
import com.kierdavis.fuchsia.model.Collection
import com.kierdavis.fuchsia.ui.component.CollectionEditorComponent
import com.kierdavis.fuchsia.ui.component.ItemCardComponent
import kotlinx.coroutines.launch

class EditCollectionFragment : Fragment(), ItemCardComponent.OnClickedListener {
    private val args: EditCollectionFragmentArgs by navArgs()
    private val viewModel by viewModels<Model> {
        Model.Factory(
            requireContext(),
            args.id
        )
    }

    private lateinit var component: CollectionEditorComponent

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        component = CollectionEditorComponent(requireContext(), viewLifecycleOwner, viewModel.liveCollection).apply {
            onItemCardClickedListener = this@EditCollectionFragment
        }
        return component.view
    }

    override fun onItemCardClicked(id: Long) {
        findNavController().navigate(
            EditCollectionFragmentDirections.actionEditCollectionToEditItem(
                id
            )
        )
    }



    class Model(private val context: Context, collectionId: Long): ViewModel() {
        val liveCollection = MutableLiveData<Collection>()

        init {
            Transformations.distinctUntilChanged(AppDatabase.getInstance(context).collectionDao().byId(collectionId)).observeForever {
                liveCollection.value = it
            }
            Transformations.distinctUntilChanged(liveCollection).observeForever {
                viewModelScope.launch {
                    AppDatabase.getInstance(context).collectionDao().update(it)
                }
            }
        }


        class Factory(private val context: Context, private val collectionId: Long): ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return Model(
                    context,
                    collectionId
                ) as T
            }
        }
    }
}
