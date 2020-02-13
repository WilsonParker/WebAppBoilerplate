package com.dev.hare.webbasetemplatemodule.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.text.TextUtils
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.dev.hare.apputilitymodule.util.RealPathUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object FileChooserManager2 {
    private const val TYPE_IMAGE = "image/*"
    const val REQUEST_CODE_NORMAL = 0x0002
    const val REQUEST_CODE_LOLLIPOP = 0x0003

    const val REQUEST_SELECT_FILE = 100
    const val FILECHOOSER_RESULTCODE = 1

    var cameraImageUri: Uri? = null
    var mFilePathCallback: ValueCallback<Uri>? = null
    var mFilePathCallbackLollipop: ValueCallback<Array<Uri>>? = null
    var mCameraPhotoPath: String? = null


    // For Android < 3.0
    fun openFileChooser(activity: Activity, uploadMsg: ValueCallback<Uri>) {
        openFileChooser(activity, uploadMsg, "")
    }

    fun imageChooser(activity: Activity) {
        var takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.run {
            if (resolveActivity(activity.packageManager) != null) {
                var photoFile = createImageFile()
                if (photoFile != null) {
                    mCameraPhotoPath = "file:$photoFile.absolutePath"
                    putExtra("PhotoPath", mCameraPhotoPath)
                    putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                } else {
                    null
                }
            }
        }

        activity.startActivityForResult(
            Intent(ACTION_CHOOSER).apply {
                putExtra(EXTRA_INTENT, Intent(ACTION_GET_CONTENT).apply {
                    addCategory(CATEGORY_OPENABLE)
                    type = TYPE_IMAGE
                })
                putExtra(EXTRA_TITLE, "File Chooser")
                putExtra(
                    EXTRA_INITIAL_INTENTS,
                    if (takePictureIntent != null) arrayOf(takePictureIntent) else arrayOfNulls<Intent>(0)
                )
            }, FileChooserManager.REQUEST_CODE
        )
    }

    // For Android 3.0+
    fun openFileChooser(activity: Activity, uploadMsg: ValueCallback<Uri>, acceptType: String) {
        mFilePathCallback = uploadMsg
        var intent = Intent(ACTION_GET_CONTENT)
        intent.addCategory(CATEGORY_OPENABLE)
        intent.type = TYPE_IMAGE
        // activity.startActivityForResult(createChooser(intent, "Image Chooser"), REQUEST_CODE_NORMAL)
        activity.startActivityForResult(createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE)
    }

    // For Android 4.1+
    fun openFileChooser(activity: Activity, uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
        openFileChooser(activity, uploadMsg, acceptType)
    }

    fun runCamera(activity: Activity, _isCapture: Boolean) {
        if (!_isCapture) {// 갤러리 띄운다.
            val pickIntent = Intent(ACTION_PICK)
            pickIntent.type = MediaStore.Images.Media.CONTENT_TYPE
            pickIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val pickTitle = "사진 가져올 방법을 선택하세요."
            val chooserIntent = createChooser(pickIntent, pickTitle)

            activity.startActivityForResult(chooserIntent, REQUEST_CODE_LOLLIPOP)
            return
        }

        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        val path = activity.filesDir
        val file = File(path, "fokCamera.png")
//        val file = createImageFile()
        // File 객체의 URI 를 얻는다.
        cameraImageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val strpa = activity.applicationContext.packageName
            FileProvider.getUriForFile(activity, "$strpa.fileprovider", file)
        } else {
            Uri.fromFile(file)
        }
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri)
        if (!_isCapture) { // 선택팝업 카메라, 갤러리 둘다 띄우고 싶을 때..
            val pickIntent = Intent(ACTION_PICK)
            pickIntent.type = MediaStore.Images.Media.CONTENT_TYPE
            pickIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val pickTitle = "사진 가져올 방법을 선택하세요."
            val chooserIntent = createChooser(pickIntent, pickTitle)

            // 카메라 intent 포함시키기..
            chooserIntent.putExtra(EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(intentCamera))
            chooserIntent.putExtra("PhotoPath", cameraImageUri)
            activity.startActivityForResult(chooserIntent, REQUEST_CODE_LOLLIPOP)
        } else {// 바로 카메라 실행..
            intentCamera.putExtra("PhotoPath", cameraImageUri)
            activity.startActivityForResult(intentCamera, REQUEST_CODE_LOLLIPOP)
        }
    }

    private fun createImageFile(): File {
        return File.createTempFile(
            "JPEG_${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}_"   // prefix
            , ".jpg"                                                                 // suffix
            , Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) // directory
        )
    }

    fun getResultUri(context: Context, data: Intent?): Uri {
        return if (data == null || TextUtils.isEmpty(data.dataString)) {
            Uri.parse(mCameraPhotoPath)
        } else {
            Uri.parse(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    data.dataString
                } else {
                    "file:" + RealPathUtil.getRealPath(context, data.data)
                }
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: WebChromeClient.FileChooserParams,
        activity: Activity
    ): Boolean {
        if (mFilePathCallbackLollipop != null) {
            mFilePathCallbackLollipop?.onReceiveValue(null)
            mFilePathCallbackLollipop = null
        }

        mFilePathCallbackLollipop = filePathCallback

        val intent = fileChooserParams.createIntent()
        try {
            activity.startActivityForResult(intent, FileChooserManager2.REQUEST_SELECT_FILE)
        } catch (e: ActivityNotFoundException) {
            mFilePathCallbackLollipop = null
            Toast.makeText(activity.applicationContext, "Cannot Open File Chooser", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        activity: Activity,
        callback: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (mFilePathCallbackLollipop == null)
                    return
                mFilePathCallbackLollipop?.onReceiveValue(
                    WebChromeClient.FileChooserParams.parseResult(
                        resultCode,
                        data
                    )
                )
                mFilePathCallbackLollipop = null
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mFilePathCallbackLollipop)
                return
// Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
// Use RESULT_OK only if you're implementing WebView inside an Activity
            var result: Uri? =
                if (data == null || resultCode != AppCompatActivity.RESULT_OK) null else data.getData()
            FileChooserManager.mUploadMessage?.onReceiveValue(result);
            FileChooserManager.mUploadMessage = null
        } else
            Toast.makeText(activity, "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }
}