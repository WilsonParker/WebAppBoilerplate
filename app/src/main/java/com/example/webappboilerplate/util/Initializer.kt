package com.example.webappboilerplate.util

import android.content.Context
import com.dev.hare.apputilitymodule.data.abstracts.Initializable
import com.dev.hare.apputilitymodule.util.Logger
import com.dev.hare.apputilitymodule.util.file.PreferenceUtil
import com.example.webappboilerplate.R
import com.dev.hare.webbasetemplatemodule.util.UrlUtil
import com.example.webappboilerplate.data.SHOW_LOGS

object Initializer : Initializable {
    override fun initialize(context: Context) {
        UrlUtil.appendSubDomain("store")
        UrlUtil.appendSubDomain("mstore")
        UrlUtil.appendSubDomain("mobile-webappboilerplate")

        PreferenceUtil.setPreferenceName(context.resources.getString(R.string.app_name))
        PreferenceUtil.initialize(context)

        Logger.IS_DEBUG = SHOW_LOGS
    }
}