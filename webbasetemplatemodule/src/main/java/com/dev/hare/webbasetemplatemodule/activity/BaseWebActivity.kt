package com.dev.hare.webbasetemplatemodule.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import com.dev.hare.apputilitymodule.util.Logger
import com.dev.hare.apputilitymodule.util.view.BackPressUtil
import com.dev.hare.webbasetemplatemodule.util.FileChooserManagerRenewer
import kotlinx.android.synthetic.main.activity_window.*
import kotlin.reflect.KClass

abstract class BaseWebActivity: BaseActivity() {
    protected abstract val windowActivity:KClass<BaseWindowActivity>

    override fun onBackPressed() {
        webview.run {
            Logger.log(Logger.LogType.INFO, "onBackPressed $url ; $host")
            /*if (isCurrentDomain(url, host) && canGoBack()) {
                goBack()
            } else {
                BackPressUtil.onBackPressed(this@BaseWebActivity) { super.onBackPressed() }
            }*/
            historyBack(host, url) {
                BackPressUtil.onBackPressed(this@BaseWebActivity) { super.onBackPressed() }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            FileChooserManagerRenewer.onActivityResult(requestCode, resultCode, data, this) { requestCode: Int, resultCode: Int, data: Intent? ->
                super.onActivityResult(requestCode, resultCode, data)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    protected fun newWindowActivity(url: Uri) {
        startActivity(Intent(this, windowActivity.java).apply { data = url })
    }

}