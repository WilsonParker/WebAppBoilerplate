package com.dev.hare.firebasepushmodule.services.abstracts

import android.app.Activity
import android.os.Build
import com.dev.hare.firebasepushmodule.services.abstracts.images.AbstractForegroundImageDownloadService
import com.google.firebase.messaging.RemoteMessage

abstract class AbstractFirebaseMessagingForegroundService<activity: Activity, service : AbstractForegroundImageDownloadService<activity>> :
    AbstractFirebaseMessagingFrame<service>() {

    override fun run(remoteMessage: RemoteMessage) {
        val intent = createIntent(remoteMessage)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(intent)
        else
            startService(intent)
    }
}