package com.kierdavis.fuchsia

import androidx.lifecycle.*

object MyTransformations {
    fun <T> flatten(lifecycleOwner: LifecycleOwner, sources: LiveData<LiveData<T>>): LiveData<T> {
        val dest = MutableLiveData<T>()
        val valueObserver = Observer<T> { dest.value = it }
        sources.observe(lifecycleOwner, Folder(lifecycleOwner, valueObserver))
        return dest
    }

    private class Folder<T>(private val lifecycleOwner: LifecycleOwner, private val valueObserver: Observer<T>) : Observer<LiveData<T>> {
        private var currentSource: LiveData<T>? = null
        override fun onChanged(newSource: LiveData<T>) {
            currentSource?.removeObserver(valueObserver)
            currentSource = newSource
            currentSource?.observe(lifecycleOwner, valueObserver)
        }
    }
}