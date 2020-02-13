package com.dev.hare.socialloginmodule.activity.basic

import com.dev.hare.apputilitymodule.util.Logger
import com.dev.hare.socialloginmodule.activity.abstracts.AbstractPaycoActivity


open class BasicPaycoActivity : AbstractPaycoActivity() {

    override fun unLink() {
        Logger.log(Logger.LogType.INFO, "unLink")
    }

    override fun redirectUrl(): String{
        return "/auth/social/payco/callback?${getRedirectUrlParam()}"
    }

}
