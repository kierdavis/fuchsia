package com.kierdavis.fuchsia.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.kierdavis.fuchsia.ui.component.Component

abstract class ComponentFragment<C: Component> : Fragment() {
    private lateinit var component: C

    abstract fun onCreateComponent(): C

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        component = onCreateComponent()
        return component.view
    }

    open val menuRes: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(menuRes != null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menuRes?.let { inflater.inflate(it, menu) }
        super.onCreateOptionsMenu(menu, inflater)
    }
}