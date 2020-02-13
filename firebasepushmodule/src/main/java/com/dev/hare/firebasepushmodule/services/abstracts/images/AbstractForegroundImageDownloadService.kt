package com.dev.hare.firebasepushmodule.services.abstracts.images

import android.app.Activity
import android.app.Notification
import android.graphics.Bitmap
import android.os.Build
import com.dev.hare.firebasepushmodule.models.abstracts.AbstractDefaultNotificationModel
import com.dev.hare.apputilitymodule.util.Logger
import com.dev.hare.apputilitymodule.util.img.ImageUtilUsingThread

abstract class AbstractForegroundImageDownloadService<activity : Activity> : AbstractImageDownloadService<activity>() {

    /**
     * Oreo 버전 이후 부터는 빈 Notification 을 사용할 수 없기 때문에
     * 새로운 Notification 을 생성하여 실행해야 합니다
     *
     * @Author : Hare
     * @Update : 19.3.27
     */
    @Throws(NullPointerException::class)
    protected fun notifyOwn(model: AbstractDefaultNotificationModel?) {
        if (model == null)
            throw NullPointerException("NotificationBuildable is null")
        startForeground(
            CHANNEL_ID,
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                model.ownNotification
            else
                Notification()
        )
    }

    protected fun notifyStop() {
        stopForeground(true)
        stopSelf(CHANNEL_ID)
    }

    /**
     * Foreground 로 Notification 을 실행하고 푸시를 보냅니다
     *
     * @Author : Hare
     * @Update : 19.3.27
     */
    override fun run() {
        try {
            notifyOwn(model)
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
                    notifyStop()
                }
            }
        }
    }

}
