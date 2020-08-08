package com.kierdavis.fuchsia

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import java.io.FileNotFoundException

object PictureCapture {
    fun launch(activity: Activity, displayName: String, launcher: ActivityResultLauncher<Uri>) {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            // XXX what mime type does the external camera activity actually produce?
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/" + FOLDER_NAME
                )
            }
        }
        val uri =
            activity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: throw RuntimeException("external content provider failed to return a Uri when creating a new picture in the media store")
        activity.getPreferences(Context.MODE_PRIVATE).edit()
            .putString(PREFERENCE_KEY, uri.toString()).apply()
        launcher.launch(uri)
    }

    fun finish(activity: Activity): Uri? {
        val uriString = activity.getPreferences(Context.MODE_PRIVATE).let { preferences ->
            val uriString = preferences.getString(PREFERENCE_KEY, null)
            preferences.edit().remove(PREFERENCE_KEY).apply()
            uriString
        } ?: return null
        val uri = Uri.parse(uriString) ?: return null
        try {
            activity.contentResolver.openInputStream(uri).use {}
        } catch (_: FileNotFoundException) {
            activity.contentResolver.delete(uri, null, null)
            return null
        }
        return uri
    }


    private const val PREFERENCE_KEY = "picture_capture_uri"

    // This should be what shows up in Google Photos as the name of the Device Folder.
    private const val FOLDER_NAME = "Fuchsia"
}