package com.dev.hare.webbasetemplatemodule.util

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.dev.hare.apputilitymodule.util.RealPathUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object FileChooserManager {
    const val REQUEST_CODE = 0x0001

    private const val TYPE_IMAGE = "image/*"
    var mUploadMessage: ValueCallback<Uri>? = null
    var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    var mCameraPhotoPath: String? = null

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
                putExtra(EXTRA_TITLE, "Image Chooser")
                putExtra(
                    EXTRA_INITIAL_INTENTS,
                    if (takePictureIntent != null) arrayOf(takePictureIntent) else arrayOfNulls<Intent>(0)
                )
            }, REQUEST_CODE
        )
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

    private fun rotate(ei: ExifInterface, bitmap: Bitmap, quality: Int): Bitmap {
        var bitmap = bitmap

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)

        // 이미지 정보 객체에서 회전에 대한 정보를 추출
        val exifOrientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        // 해당 정보를 기반으로 int 회전각 수치를 추출
        val exifDegree = exifOrientationToDegrees(exifOrientation)

        if (exifDegree != 0 && bitmap != null) {
            val m = Matrix()

            // 회전각을 적용 시키고 일단은 해당 사진 크기의 절반 정도 크기로 줄인다
            m.setRotate(exifDegree as Float, bitmap.width as Float, bitmap.height as Float)

            try {
                // 회전 !
                val converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)

                if (bitmap != converted) {
                    bitmap.recycle()
                    bitmap = converted
                }
            } catch (ex: Exception) {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환.
                ex.printStackTrace()
            }

        }
        return bitmap
    }

    private fun exifOrientationToDegrees(exifOrientation: Int): Int {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270
        }
        return 0
    }

    fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: WebChromeClient.FileChooserParams?,
        activity: Activity
    ): Boolean {
        mFilePathCallback?.onReceiveValue(null)
        mFilePathCallback = filePathCallback
        imageChooser(activity)
        return true
    }

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        activity: Activity,
        callback: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit
    ) {
        when (resultCode) {
            RESULT_OK -> when (requestCode) {
                REQUEST_CODE -> {
                    FileChooserManager.run {
                        if (requestCode === REQUEST_CODE && resultCode === RESULT_OK) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                if (mFilePathCallback == null) {
                                    callback(requestCode, resultCode, data)
                                    return
                                }
                                val results = arrayOf(getResultUri(activity, data))
                                mFilePathCallback?.onReceiveValue(results)
                                mFilePathCallback = null
                            } else {
                                if (mUploadMessage == null) {
                                    callback(requestCode, resultCode, data)
                                    return
                                }
                                val result = getResultUri(activity, data)

                                // Logger.log(Logger.LogType.INFO,"imageChooser : $result")
                                mUploadMessage?.onReceiveValue(result)
                                mUploadMessage = null
                            }
                        } else {
                            mFilePathCallback?.onReceiveValue(null)
                            mUploadMessage?.onReceiveValue(null)
                            mFilePathCallback = null
                            mUploadMessage = null
                            callback(requestCode, resultCode, data)
                        }
                        callback(requestCode, resultCode, data)
                    }
                }
            }
        }
    }
}