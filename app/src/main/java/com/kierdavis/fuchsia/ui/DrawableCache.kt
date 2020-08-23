package com.kierdavis.fuchsia.ui

import android.content.ContentResolver
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.collection.LruCache

class DrawableCache(private val resolver: ContentResolver) : LruCache<Uri, Drawable>(256) {
    override fun create(uri: Uri): Drawable? =
        resolver.openInputStream(uri)?.use { stream ->
            Drawable.createFromStream(stream, uri.toString())
        }

    companion object {
        @Volatile
        private var theInstance: DrawableCache? = null

        fun getInstance(context: Context): DrawableCache {
            return theInstance
                ?: synchronized(this) {
                    DrawableCache(context.contentResolver)
                        .also { theInstance = it }
                }
        }
    }
}