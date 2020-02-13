package com.dev.hare.firebasepushmodule.services.abstracts.images

import android.app.Activity
import android.graphics.Bitmap
import com.dev.hare.apputilitymodule.util.Logger
import com.dev.hare.apputilitymodule.util.img.ImageUtilUsingThread

abstract class AbstractBackgroundImageDownloadService<activity : Activity> : AbstractImageDownloadService<activity>() {

    /**
     * Foreground 로 Notification 을 실행하고 푸시를 보냅니다
     *
     * @Author : Hare
     * @Update : 19.3.27
     */
    override fun run() {
        try {
            model!!.let {
                val imageUtilUsingThread = ImageUtilUsingThread()
                imageUtilUsingThread.urlToBitmapUsingThread(
                    url,
                    null,
                    onImageLoadCompleteListener
                )
            }
        } catch (e: Exception) {
            Logger.log(Logger.LogType.ERROR, "", e)
        }
    }


    override fun createOnImageLoadCompleteListener(): ImageUtilUsingThread.OnImageLoadCompleteListener {
        return object : ImageUtilUsingThread.OnImageLoadCompleteListener {
            override fun onComplete(bitmap: Bitmap?) {
                model?.run {
                    pictureStyleModel.bigPicture = bitmap
                    runNotification()
                }
            }
        }
    }
}
