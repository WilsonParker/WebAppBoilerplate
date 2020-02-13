package com.dev.hare.firebasepushmodule.services.abstracts

import android.app.Service
import android.content.Intent
import com.dev.hare.firebasepushmodule.services.abstracts.images.AbstractImageDownloadService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.reflect.KClass

abstract class AbstractFirebaseMessagingFrame<service: Service> : FirebaseMessagingService() {
    protected abstract val serviceClass: KClass<service>

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if (remoteMessage != null && remoteMessage.data != null && remoteMessage.data.isNotEmpty()) {
            run(remoteMessage)
        }
    }

    protected abstract fun run(remoteMessage: RemoteMessage)

    /**
     * Intent 를 create, setting 합니다
     *
     * @Author : Hare
     * @Updated : 19.3.27
     */
    // protected abstract fun createIntent(remoteMessage: RemoteMessage): Intent
    protected fun createIntent(remoteMessage: RemoteMessage): Intent {
        val intent = Intent()
        intent.setClass(baseContext, serviceClass.java)
        intent.putExtra(AbstractImageDownloadService.KEY_URL, remoteMessage.data[AbstractImageDownloadService.KEY_URL])
        intent.putExtra(AbstractImageDownloadService.KEY_REMOTE_MESSAGE, remoteMessage)
        return intent
    }
}