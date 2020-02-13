package com.dev.hare.socialloginmodule.activity.abstracts

import android.app.Activity
import android.content.Intent
import com.dev.hare.apputilitymodule.util.Logger
import com.toast.android.paycologin.*


abstract class AbstractPaycoActivity : AbstractSocialActivity() {
    companion object {
        const val REQUEST_CODE = 0x0014

        fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
            PaycoLoginManager.getInstance().onActivityResult(activity, requestCode, resultCode, data)
        }
    }

    override val socialName: String = "payco"
    protected open val onLoginListener: OnLoginListener
        get() = object : OnLoginListener {
            override fun onFail(p0: PaycoLoginError?) {
                Logger.log(Logger.LogType.ERROR, "onFail")
                finish()
            }

            override fun onCancel() {
                Logger.log(Logger.LogType.ERROR, "onCancel")
            }

            override fun onLogin(p0: PaycoLoginExtraResult?) {
                var manager = PaycoLoginManager.getInstance()
                returnMainActivity(manager.accessToken, "", manager.loginId)
            }
        }
    protected open val onLogoutListener: OnLogoutListener
        get() = object : OnLogoutListener {
            override fun onFail(p0: PaycoLoginError?) {
                Logger.log(Logger.LogType.ERROR, "onFail")
            }

            override fun onLogout() {
                Logger.log(Logger.LogType.ERROR, "onLogout")
            }
        }

    override fun logIn() {
        PaycoLoginManager.getInstance().login(this, onLoginListener)
    }

    override fun logOut() {
        PaycoLoginManager.getInstance().logout(onLogoutListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        PaycoLoginManager.getInstance().onActivityResult(this, requestCode, resultCode, data)
    }
}
