package com.example.webappboilerplate.push

import com.dev.hare.firebasepushmodule.services.abstracts.AbstractFirebaseMessagingForegroundService
import com.example.webappboilerplate.activity.MainWithIntroActivity

class BasicMessagingListener: AbstractFirebaseMessagingForegroundService<MainWithIntroActivity, ImageDownloadService>() {
    override val serviceClass = ImageDownloadService::class
}