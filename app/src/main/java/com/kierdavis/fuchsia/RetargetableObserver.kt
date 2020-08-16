package com.kierdavis.fuchsia

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

abstract class RetargetableObserver<T>(private val lifecycleOwner: LifecycleOwner) : Observer<T> {
    var observedData: LiveData<T>? = null
        set(newObservedData) {
            observedData?.removeObserver(this)
            field = newObservedData
            observedData?.observe(lifecycleOwner, this)
        }
}