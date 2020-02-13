package com.dev.hare.apputilitymodule.util.img

import android.graphics.Bitmap
import android.os.AsyncTask
import com.dev.hare.apputilitymodule.exception.ExceptionUtil

class ImageUtilUsingThread : ImageUtil() {
    protected var onImageLoadCompleteListener: OnImageLoadCompleteListener? = null
    private var def: Bitmap? = null
    private var onErrorUtil: ExceptionUtil? = null

    fun urlToBitmapUsingThread(
        strUrl: String?,
        def: Bitmap?,
        onImageLoadCompleteListener: OnImageLoadCompleteListener
    ) {
        this.def = def
        this.onImageLoadCompleteListener = onImageLoadCompleteListener
        ImageLoadTask().execute(strUrl)
    }

    interface OnImageLoadCompleteListener {
        fun onComplete(bitmap: Bitmap?)
    }

    private inner class ImageLoadTask : AsyncTask<String, Void, Bitmap?>() {
        override fun doInBackground(vararg strings: String?): Bitmap? {
            try {
                return strings[0]?.let {
                    if (it.isNotEmpty()) {
                        onErrorUtil = ExceptionUtil(
                            6,
                            onExecute = object : ExceptionUtil.OnExecute {
                                override fun <T> execute(): T {
                                    var bitmap = urlToBitmap(strings[0]!!)
                                    return bitmap as T
                                }
                            }
                        )
                        return onErrorUtil!!.handleException(def)
                    } else {
                        return null
                    }
                }
            } catch (e: Exception) {
                return null
            }

        }

        override fun onPostExecute(bitmap: Bitmap?) {
            onImageLoadCompleteListener!!.onComplete(bitmap)
        }
    }

}