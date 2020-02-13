package com.example.webappboilerplate.push

import android.app.PendingIntent
import android.content.Context
import com.dev.hare.firebasepushmodule.basic.FirebaseBasicModel
import com.dev.hare.firebasepushmodule.models.NotificationBuilderModel
import com.dev.hare.firebasepushmodule.models.NotificationDataModel
import com.dev.hare.firebasepushmodule.models.abstracts.AbstractDefaultNotificationModel
import com.dev.hare.firebasepushmodule.services.abstracts.images.AbstractForegroundImageDownloadService
import com.example.webappboilerplate.R
import com.example.webappboilerplate.activity.MainWithIntroActivity
import kotlin.reflect.KClass

class ImageDownloadService : AbstractForegroundImageDownloadService<MainWithIntroActivity>() {
    override val icon = R.mipmap.ic_launcher
    override val activityClass: KClass<MainWithIntroActivity>
        get() = MainWithIntroActivity::class
    override val _channelID = "BasicWebAppChannel"
    override val _channelName = "BasicWebApp"

    override fun createModel(
        context: Context,
        dataModel: NotificationDataModel,
        builderModel: NotificationBuilderModel,
        pendingIntent: PendingIntent
    ): AbstractDefaultNotificationModel {
        return FirebaseBasicModel(context, dataModel, builderModel, pendingIntent)
    }
}