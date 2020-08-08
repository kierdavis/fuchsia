package com.kierdavis.fuchsia.ui

import android.content.ContentResolver
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.collection.LruCache

class PictureDrawableCache(private val resolver: ContentResolver) : LruCache<Uri, Drawable>(256) {
    override fun create(uri: Uri): Drawable? =
        resolver.openInputStream(uri)?.use { stream ->
            Drawable.createFromStream(stream, uri.toString())
        }

    companion object {
        @Volatile
        private var theInstance: PictureDrawableCache? = null

        fun getInstance(context: Context): PictureDrawableCache {
            return theInstance
                ?: synchronized(this) {
                PictureDrawableCache(context.contentResolver)
                    .also {
                    theInstance = it
                }
            }
        }
    }
}