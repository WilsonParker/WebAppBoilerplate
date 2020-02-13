package com.dev.hare.webbasetemplatemodule.activity

import android.os.Bundle
import android.webkit.WebView

abstract class BaseWindowActivity: BaseWebActivity() {
    protected abstract val webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateInit(savedInstanceState)
        onCreateAfter(savedInstanceState)
    }

    protected fun getUrl(): String {
        return intent.data.toString()
    }

    override fun onBackPressed() {
        finish()
    }
}
