package com.kierdavis.fuchsia.ui.component

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.kierdavis.fuchsia.database.AppDatabase
import com.kierdavis.fuchsia.ui.DrawableCache

abstract class Component(val context: Context, val lifecycleOwner: LifecycleOwner) {
    abstract val view: View

    val database
        get() = AppDatabase.getInstance(context)
    val drawableCache
        get() = DrawableCache.getInstance(context)
}