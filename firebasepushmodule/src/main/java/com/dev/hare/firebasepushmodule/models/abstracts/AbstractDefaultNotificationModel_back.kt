package com.dev.hare.firebasepushmodule.models.abstracts

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.dev.hare.firebasepushmodule.example.ExampleMainActivity
import com.dev.hare.firebasepushmodule.models.interfaces.NotificationBuildable_back
import kotlin.reflect.KClass

abstract class AbstractDefaultNotificationModel_back(
    protected val context: Context,
    data: Map<String, String>,
    protected val channelID: String = "channelID",
    protected val channelName: String = "channelName"
) : NotificationBuildable_back {

    companion object {
        const val KEY_TITLE = "title"
        const val KEY_CONTENT = "content"
        const val KEY_IMAGE_URL = "imageUrl"
        const val KEY_LINK = "link"
        const val KEY_PUSH_TYPE = "push_type"
    }

    abstract protected val icon: Int

    protected val _REQUEST_CODE = 0

    protected var title: String? = null
    protected var content: String? = null
    protected var link: String? = null
    protected var imageUrl: String? = null
    protected var pushType: String? = null
    protected var notificationManager: NotificationManager =
        context.getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager

    var img: Bitmap? = null
    var ownNotification: Notification? = null
        @RequiresApi(Build.VERSION_CODES.O)
        get(){
            notificationManager.createNotificationChannel(notificationChannel)
            if(field == null) field = createOwnNotification()
            return field
        }
    protected var notificationChannel: NotificationChannel? = null
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            if(field == null) field = createNotificationChannel(notificationManager)
            return field
        }
    protected var notificationBuilder: Notification.Builder? = null
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            if(field == null) field = applyNotificationBuilder(createNotificationBuilder())
            return field
        }
    protected var notificationCompatBuilder: NotificationCompat.Builder? = null
        get() {
            if(field == null) field = applyNotificationCompatBuilder(createNotificationCompatBuilder())
            return field
        }
    protected var pendingIntent: PendingIntent? = null
        get() {
            if(field == null) field = createPendingIntent(ExampleMainActivity::class)
            return field
        }

    init {
        this.title = data[KEY_TITLE]
        this.content = data[KEY_CONTENT]
        this.imageUrl = data[KEY_IMAGE_URL]
        this.link = data[KEY_LINK]
        this.pushType = data[KEY_PUSH_TYPE]
    }

    /**
     * create default foreground Notification
     *
     * @param
     * @return
     * @author Hare
     * @added 28/03/2019
     * @updated 28/03/2019
     * */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun createDefaultOwnNotification(): Notification {
        createDefaultNotificationChannel(notificationManager)
        notificationBuilder = Notification.Builder(context, channelID)
        return notificationBuilder?.apply {
            setOngoing(true)
            setCategory(Notification.CATEGORY_SERVICE)
            setContentIntent(pendingIntent)
        }!!.build()
    }

    /**
     * create NotificationChannel in NotificationManager
     *
     * @param
     * @return
     * @author Hare
     * @added 28/03/2019
     * @updated 28/03/2019
     * */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun createDefaultNotificationChannel(notificationManager: NotificationManager): NotificationChannel {
        return NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            enableLights(true)
            enableVibration(true)
            importance = NotificationManager.IMPORTANCE_DEFAULT
        }
    }

    /**
     * create NotificationCompat.Builder by default
     *
     * @param
     * @return
     * @author Hare
     * @added 28/03/2019
     * @updated 28/03/2019
     * */
    override fun createDefaultNotificationCompatBuilder(): NotificationCompat.Builder {
        notificationCompatBuilder = NotificationCompat.Builder(context, channelID)
        return notificationCompatBuilder!!.apply {
            setOngoing(true)
            setSmallIcon(icon)
            setContentTitle(title)
            setContentText(content)
            setCategory(Notification.CATEGORY_SERVICE)
            setContentIntent(createDefaultPendingIntent(ExampleMainActivity::class))
        }
    }

    /**
     * create Notification.Builder by default
     *
     * @param
     * @return
     * @author Hare
     * @added 28/03/2019
     * @updated 28/03/2019
     * */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun createDefaultNotificationBuilder(): Notification.Builder {
        notificationBuilder = Notification.Builder(context, channelID)
        return notificationBuilder!!.apply {
            setOngoing(true)
            setSmallIcon(icon)
            setContentTitle(title)
            setContentText(content)
            setCategory(Notification.CATEGORY_SERVICE)
            setContentIntent(pendingIntent)
        }
    }

    /**
     * create PendingIntent by default
     *
     * @param
     * @return
     * @author Hare
     * @added 28/03/2019
     * @updated 28/03/2019
     * */
    override fun createDefaultPendingIntent(activity: KClass<out Activity>): PendingIntent {
        var intent = Intent(context, activity.java)
        return PendingIntent.getActivity(
            context,
            _REQUEST_CODE,
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    /**
     * apply default NotificationCompat.BigPictureStyle to NotificationCompat.Builder
     *
     * @param
     * @return
     * @author Hare
     * @added 28/03/2019
     * @updated 28/03/2019
     * */
    fun applyDefaultBigPictureStyle(builder: NotificationCompat.Builder, image: Bitmap): NotificationCompat.Builder {
        return builder.apply {
            setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(image)
                    .setBigContentTitle(title)
                    .setSummaryText(content)
            )
        }
    }

    /**
     * apply default Notification.BigPictureStyle to Notification.Builder
     *
     * @param
     * @return
     * @author Hare
     * @added 28/03/2019
     * @updated 28/03/2019
     * */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun applyDefaultBigPictureStyle(builder: Notification.Builder, image: Bitmap): Notification.Builder {
        return builder.apply {
            style = Notification.BigPictureStyle()
                .bigPicture(image)
                .setBigContentTitle(title)
                .setSummaryText(content)
        }
    }

    /**
     * apply default NotificationCompat.BigTextStyle to NotificationCompat.Builder by default
     *
     * @param
     * @return
     * @author Hare
     * @added 28/03/2019
     * @updated 28/03/2019
     * */
    fun applyDefaultBigTextStyle(builder: NotificationCompat.Builder): NotificationCompat.Builder {
        return builder.apply {
            setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(content)
                    .setBigContentTitle(title)
                    .setSummaryText(content)
            )
        }
    }

    /**
     * apply default Notification.BigTextStyle to Notification.Builder
     *
     * @param
     * @return
     * @author Hare
     * @added 28/03/2019
     * @updated 28/03/2019
     * */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun applyDefaultBigTextStyle(builder: Notification.Builder): Notification.Builder {
        return builder.apply {
            style = Notification.BigTextStyle()
                .bigText(content)
                .setBigContentTitle(title)
                .setSummaryText(content)
        }
    }

    /**
     * notify
     *
     * @param
     * @return
     * @author Hare
     * @added 28/03/2019
     * @updated 03/04/2019
     * */
    fun runNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(0, notificationBuilder!!.build())
        } else {
            notificationManager.notify(0, notificationCompatBuilder!!.build())
        }
    }

    abstract fun applyNotificationCompatBuilder(notificationCompatBuilder: NotificationCompat.Builder): NotificationCompat.Builder
    abstract fun applyNotificationBuilder(notificationBuilder: Notification.Builder): Notification.Builder
}