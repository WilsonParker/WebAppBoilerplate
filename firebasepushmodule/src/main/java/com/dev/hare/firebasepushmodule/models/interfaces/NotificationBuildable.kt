package com.dev.hare.firebasepushmodule.models.interfaces

import android.app.*
import android.os.Build
import androidx.annotation.RequiresApi

interface NotificationBuildable {
    fun createDefaultOwnNotification(): Notification

    fun createOwnNotification(): Notification

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createDefaultNotificationChannel(notificationManager: NotificationManager): NotificationChannel

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createNotificationChannel(notificationManager: NotificationManager): NotificationChannel

}