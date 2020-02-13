package com.dev.hare.apputilitymodule.util

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.loader.content.CursorLoader

object RealPathUtil {

    fun getRealPath(context: Context, uri: Uri): String? {
        return when {
            // SDK < API11
            Build.VERSION.SDK_INT < 11 -> getRealPathFromURI_BelowAPI11(context, uri)

            // SDK >= 11 && SDK < 19
            Build.VERSION.SDK_INT < 19 -> getRealPathFromURI_API11to18(context, uri)

            // SDK > 19 (Android 4.4)
            else -> getRealPathFromURI_API19(context, uri).toString()
        }
    }

    @SuppressLint("NewApi")
    fun getRealPathFromURI_API19(context: Context, uri: Uri): String? {
        // check here to KITKAT or new version
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                var split = docId.split(":")
                var type = split[0]

                if ("primary".equals(type, true)) {
                    return "${Environment.getExternalStorageDirectory()}/${split[1]}"
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    id.toLong()
                )

                return getDataColumn(context, contentUri, null, null)
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val type = split[0]

                var contentUri: Uri? = when (type) {
                    "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    else -> null
                }

                val selection = "_id=?";
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(
                    context, contentUri, selection,
                    selectionArgs
                )
            }
        }
        // MediaStore (and general)
        else if ("content".equals(uri.scheme, true)) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.lastPathSegment

            return getDataColumn(context, uri, null, null)
        }
        // File
        else if ("file".equals(uri.scheme, true)) {
            return uri.path
        }
        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        val column = "_data"
        val projection: Array<String> = arrayOf(column)

        var cursor = context.contentResolver.query(
            uri, projection,
            selection, selectionArgs, null
        )
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndexOrThrow(column))
        }
        cursor?.close()
        return null
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    @SuppressLint("NewApi")
    fun getRealPathFromURI_API11to18(context: Context, contentUri: Uri): String? {
        var proj = arrayOf(MediaStore.Images.Media.DATA)
        var result: String? = null

        var cursorLoader = CursorLoader(
            context,
            contentUri, proj, null, null, null
        )
        var cursor = cursorLoader.loadInBackground()

        if (cursor != null) {
            var columnIndex =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
        }
        return result
    }

    fun getRealPathFromURI_BelowAPI11(context: Context, contentUri: Uri): String {
        var proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = context.contentResolver.query(contentUri, proj, null, null, null)
        var column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }
}