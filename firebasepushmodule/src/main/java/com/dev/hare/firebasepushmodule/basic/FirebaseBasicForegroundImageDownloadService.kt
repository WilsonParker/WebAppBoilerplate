package com.dev.hare.firebasepushmodule.basic

import android.app.PendingIntent
import android.content.Context
import com.dev.hare.firebasepushmodule.R
import com.dev.hare.firebasepushmodule.example.ExampleMainActivity
import com.dev.hare.firebasepushmodule.models.NotificationBuilderModel
import com.dev.hare.firebasepushmodule.models.NotificationDataModel
import com.dev.hare.firebasepushmodule.models.abstracts.AbstractDefaultNotificationModel
import com.dev.hare.firebasepushmodule.services.abstracts.images.AbstractForegroundImageDownloadService
import kotlin.reflect.KClass

class FirebaseBasicForegroundImageDownloadService : AbstractForegroundImageDownloadService<ExampleMainActivity>() {
    override val icon = R.mipmap.ic_launcher
    override val activityClass: KClass<ExampleMainActivity>
        get() = ExampleMainActivity::class
    override val _channelID = "FirebasePushModule_ID"
    override val _channelName = "FirebasePushModule_Name"

    override fun createModel(
        context: Context,
        dataModel: NotificationDataModel,
        builderModel: NotificationBuilderModel,
        pendingIntent: PendingIntent
    ): AbstractDefaultNotificationModel {
        return FirebaseBasicModel(context, dataModel, builderModel, pendingIntent)
    }
}