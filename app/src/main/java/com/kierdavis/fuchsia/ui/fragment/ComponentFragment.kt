package com.kierdavis.fuchsia.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
}