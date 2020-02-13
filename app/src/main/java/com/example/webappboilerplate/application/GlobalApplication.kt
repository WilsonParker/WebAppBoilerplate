package com.example.webappboilerplate.application

import com.dev.hare.socialloginmodule.application.GlobalApplication
import com.example.webappboilerplate.util.Initializer

class GlobalApplication : GlobalApplication() {
    override fun onCreate() {
        super.onCreate()
//        PushModuleInitializer.initialize(this)
        Initializer.initialize(this)
    }
}
