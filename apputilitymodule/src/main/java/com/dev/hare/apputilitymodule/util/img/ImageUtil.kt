package com.dev.hare.apputilitymodule.util.img

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder

open class ImageUtil {
    private val _connectTimeOut = 60 /* Second */ * 1000

    /**
     * NotificationBuilder 를 생성
     * @Author : Hare
     * @Update : 19.3.26
     */
    @Throws(Exception::class)
    fun urlToBitmap(strUrl: String): Bitmap {
        val bitmap: Bitmap?
        val inputStream: InputStream
        val url = URL(URLDecoder.decode(strUrl, "EUC-KR"))
        val connection = (url.openConnection() as HttpURLConnection).apply {
            connectTimeout = _connectTimeOut
            readTimeout = _connectTimeOut
            requestMethod = "GET"
            doInput = true
            //        connection.setInstanceFollowRedirects(true);
            connect()
        }

        val responseCode = connection.responseCode
        if (responseCode != HttpURLConnection.HTTP_OK) {
            connection.disconnect()
            throw IllegalAccessException("Fail to connect Server")
        }
        inputStream = connection.inputStream
        bitmap = BitmapFactory.decodeStream(inputStream)
        connection.disconnect()
        if (bitmap == null)
            throw NullPointerException("Bitmap is null")
        return bitmap
    }

    @Throws(IOException::class)
    fun urlToBitmapWithFactory(strUrl: String): Bitmap {
        val bitmap: Bitmap
        val url = URL(strUrl)
        bitmap = BitmapFactory.decodeStream(url.content as InputStream)
        return bitmap
    }

    fun getDrawableWithID(context: Context, id:Int, theme: Resources.Theme?): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.resources.getDrawable(id, theme)
        } else {
            context.resources.getDrawable(id)
        }
    }

}