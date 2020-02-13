package com.example.webappboilerplate.activity

import android.os.Bundle
import com.dev.hare.apputilitymodule.util.Logger
import com.example.webappboilerplate.R
import com.example.webappboilerplate.web.AndroidBridge
import com.example.webappboilerplate.web.CustomWebView
import com.dev.hare.webbasetemplatemodule.activity.BaseWindowActivity
import kotlinx.android.synthetic.main.activity_window.*
import kotlin.reflect.KClass

class WindowActivity : BaseWindowActivity() {
    override val windowActivity: KClass<BaseWindowActivity> = WindowActivity::class as KClass<BaseWindowActivity>
    override val webView: CustomWebView
        get() = window_webview

    override fun onCreateInit(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_window)
        webView.host = getUrl()
        webView.webViewCommand?.openable = false
        webView.javascriptBrideInterface = AndroidBridge(this, webView)
        webView.initWebView(this)
        Logger.log(Logger.LogType.INFO, "WEBVIEW NEW WINDOW ${getUrl()}")
    }

    override fun onCreateAfter(savedInstanceState: Bundle?) {
    }

}
