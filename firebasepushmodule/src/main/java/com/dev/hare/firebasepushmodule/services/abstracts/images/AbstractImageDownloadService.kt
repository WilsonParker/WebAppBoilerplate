package com.dev.hare.firebasepushmodule.services.abstracts.images

import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.dev.hare.firebasepushmodule.models.NotificationBuilderModel
import com.dev.hare.firebasepushmodule.models.NotificationDataModel
import com.dev.hare.firebasepushmodule.models.abstracts.AbstractDefaultNotificationModel
import com.dev.hare.apputilitymodule.util.img.ImageUtilUsingThread
import com.google.firebase.messaging.RemoteMessage
import kotlin.reflect.KClass

abstract class AbstractImageDownloadService<activity : Activity> : Service() {

    companion object {
        const val KEY_URL = AbstractDefaultNotificationModel.Key.IMAGE_URL
        const val KEY_REMOTE_MESSAGE = "msg"
        const val KEY_LINK = "link"
        const val _REQUEST_CODE = 0x0010
        const val CHANNEL_ID = 1
        var model: AbstractDefaultNotificationModel? = null
    }

    abstract protected val _channelID: String
    abstract protected val _channelName: String
    abstract protected val activityClass: KClass<activity>
    abstract protected val icon: Int

    protected var url: String? = null
    protected var data: Map<String, String>? = null
    protected var onImageLoadCompleteListener: ImageUtilUsingThread.OnImageLoadCompleteListener =
        createOnImageLoadCompleteListener()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        runAfterBind(intent)
        return START_REDELIVER_INTENT
    }

    /**
     * RemoteMessage 를 이용해서 imageUrl, data property 에 값을 대입 한 후 run() 을 실행합니다
     *
     * @Author : Hare
     * @Update : 19.3.27
     */
    private fun runAfterBind(intent: Intent?) {
        intent?.let {
            url = it.getStringExtra(KEY_URL)
            data = (it.getParcelableExtra(KEY_REMOTE_MESSAGE) as RemoteMessage).data
            model = createNotificationModel(data)
            run()
        }
    }

    /**
     * create PendingIntent by default
     *
     * @param
     * @return
     * @author Hare
     * @added 28/03/2019
     * @updated 09/05/2019
     * */
    fun createDefaultPendingIntent(activity: KClass<out Activity>, link:String? = ""): PendingIntent {
        var intent = Intent(this, activity.java).apply {
            putExtra(KEY_LINK, link)
        }
        return PendingIntent.getActivity(
            this,
            _REQUEST_CODE,
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    protected fun createNotificationModel(data: Map<String, String>?): AbstractDefaultNotificationModel {
        var dataModel = NotificationDataModel(this, _channelID, _channelName, data)
        var pendingIntent = createDefaultPendingIntent(activityClass, dataModel?.link)
        var builderModel = NotificationBuilderModel(this, _channelID).apply {
            // dataModel.title?.let { setContentTitle(it) }
            // dataModel.content?.let { setContentText(it) }
            // setSubText()
            setSmallIcon(icon)
            setAutoCancel(true)
            setPriority(Notification.PRIORITY_MAX)
            setContentIntent(pendingIntent)
        }
        return createModel(this, dataModel, builderModel, pendingIntent)
    }

    abstract fun run()
    abstract fun createOnImageLoadCompleteListener(): ImageUtilUsingThread.OnImageLoadCompleteListener
    abstract fun createModel(context: Context, dataModel: NotificationDataModel, builderModel: NotificationBuilderModel, pendingIntent: PendingIntent): AbstractDefaultNotificationModel
}
