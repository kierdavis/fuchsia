package com.kierdavis.fuchsia

import android.content.ContentValues
import android.content.Context
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.kierdavis.fuchsia.database.AppDatabase
import com.kierdavis.fuchsia.model.TemporaryMediaUri
import java.io.FileNotFoundException

object TemporaryMediaUriManager {
    suspend fun prune(context: Context) {
        val database = AppDatabase.getInstance(context)
        val entities = database.temporaryMediaUriDao().getAll()
        entities.forEach { context.contentResolver.delete(it.uri, null, null) }
        database.temporaryMediaUriDao().delete(entities)
    }

    suspend fun create(context: Context): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "fuchsia_capture")
            // TODO confirm what mime type the external camera activity actually produces
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/" + FOLDER_NAME)
            }
        }
        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues) ?: return null
        AppDatabase.getInstance(context).temporaryMediaUriDao().insert(TemporaryMediaUri(uri))
        return uri
    }

    suspend fun makePermanent(context: Context, reqUri: Uri? = null): Uri? {
        val database = AppDatabase.getInstance(context)
        val entity = database.temporaryMediaUriDao().get() ?: return null
        database.temporaryMediaUriDao().delete(entity)
        if (reqUri != null && entity.uri != reqUri) {
            return null
        }
        try {
            context.contentResolver.openInputStream(entity.uri).use {}
        } catch (_: FileNotFoundException) {
            context.contentResolver.delete(entity.uri, null, null)
            return null
        }
        return entity.uri
    }

    // This is what shows up in Google Photos as the name of the Device Folder.
    private const val FOLDER_NAME = "Fuchsia"

}