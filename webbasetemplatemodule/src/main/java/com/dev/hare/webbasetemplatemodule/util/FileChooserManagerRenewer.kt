package com.dev.hare.webbasetemplatemodule.util

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.annotation.RequiresApi
import com.dev.hare.apputilitymodule.util.Logger
import com.dev.hare.apputilitymodule.util.RealPathUtil
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


object FileChooserManagerRenewer {
    private const val TYPE_IMAGE = "image/*"
    const val REQUEST_CODE_NORMAL = 0x0002
    const val REQUEST_CODE_LOLLIPOP = 0x0003

    var mUploadMessage: ValueCallback<Uri>? = null
    var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    var mCameraPhotoPath: String? = null

    // 출처: https://gogorchg.tistory.com/entry/Android-WebView-File-Upload [항상 초심으로]
    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        var timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "JPEG_" + timeStamp + "_"
        var storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        var imageFile = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */
        )
        return imageFile;
    }


    // 출처@ https@ //gogorchg.tistory.com/entry/Android-WebView-File-Upload [항상 초심으로]
    fun getResultUri(context: Context, data: Intent?): Uri? {
        var result: Uri? = null
        if (data == null || TextUtils.isEmpty(data.dataString)) {
            // If there is not data, then we may have taken a photo
            if (mCameraPhotoPath != null) {
                result = Uri.parse(mCameraPhotoPath)
            }
        } else {
            val filePath: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                data.dataString
            } else {
                "file:" + RealPathUtil.getRealPath(context, data.data)!!
            }
            result = Uri.parse(filePath)
        }

        return result
    }

    fun imageChooser(activity: Activity) {
        var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent?.resolveActivity(activity.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                takePictureIntent?.putExtra("PhotoPath", mCameraPhotoPath)
            } catch (ex: IOException) {
                // Error occurred while creating the File
                Logger.log(Logger.LogType.ERROR, "Unable to create Image File", ex)
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.getAbsolutePath()
                takePictureIntent?.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile)
                )
            } else {
                takePictureIntent = null
            }
        }

        var contentSelectionIntent = Intent(ACTION_GET_CONTENT)
        contentSelectionIntent.addCategory(CATEGORY_OPENABLE)
        contentSelectionIntent.type = TYPE_IMAGE

        var intentArray: Array<Intent>
        if (takePictureIntent != null) {
            intentArray = arrayOf(takePictureIntent)
        } else {
            intentArray = arrayOf()
        }

        var chooserIntent = Intent(ACTION_CHOOSER);
        chooserIntent.putExtra(EXTRA_INTENT, contentSelectionIntent)
        chooserIntent.putExtra(EXTRA_TITLE, "Image Chooser")
        chooserIntent.putExtra(EXTRA_INITIAL_INTENTS, intentArray)

        activity.startActivityForResult(chooserIntent, REQUEST_CODE_NORMAL)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: WebChromeClient.FileChooserParams,
        activity: Activity
    ): Boolean {
        if (mFilePathCallback != null) {
            mFilePathCallback?.onReceiveValue(null)
        }
        mFilePathCallback = filePathCallback
        imageChooser(activity)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        activity: Activity,
        callback: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit
    ) {
        if (requestCode === REQUEST_CODE_NORMAL && resultCode === RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (mFilePathCallback == null) {
                    callback(requestCode, resultCode, data)
                    return
                }
                getResultUri(activity, data)?.let {
                    val results = arrayOf(it)
                    mFilePathCallback?.onReceiveValue(results)
                }

                mFilePathCallback = null
            } else {
                if (mUploadMessage == null) {
                    callback(requestCode, resultCode, data)
                    return
                }
                val result = getResultUri(activity, data)

                Logger.log(Logger.LogType.INFO, "openFileChooser : $result")
                mUploadMessage?.onReceiveValue(result)
                mUploadMessage = null
            }
        } else {
            if (mFilePathCallback != null) mFilePathCallback?.onReceiveValue(null)
            if (mUploadMessage != null) mUploadMessage?.onReceiveValue(null)
            mFilePathCallback = null
            mUploadMessage = null
            callback(requestCode, resultCode, data)
        }
    }
}