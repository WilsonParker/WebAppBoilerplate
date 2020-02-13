package com.example.webappboilerplate.web

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.webkit.JavascriptInterface
import com.dev.hare.firebasepushmodule.http.model.HttpConstantModel
import com.dev.hare.firebasepushmodule.util.FirebaseUtil
import com.dev.hare.apputilitymodule.util.Logger
import com.dev.hare.socialloginmodule.activity.abstracts.*
import com.example.webappboilerplate.social.BasicFacebookActivity
import com.example.webappboilerplate.social.BasicKakaoActivity
import com.example.webappboilerplate.social.BasicNaverActivity
import com.example.webappboilerplate.social.BasicPaycoActivity
import com.example.webappboilerplate.activity.MainWithIntroActivity.Companion.mainWebView
import com.example.webappboilerplate.push.BasicTokenCallService
import com.dev.hare.webbasetemplatemodule.web.BaseWebView
import com.dev.hare.webbasetemplatemodule.web.BaseWebViewCommand
import com.gun0912.tedpermission.TedPermission
import com.example.webappboilerplate.activity.WindowActivity
import com.dev.hare.webbasetemplatemodule.util.UrlUtil
import com.dev.hare.webbasetemplatemodule.web.BaseWebViewClient
import com.gun0912.tedpermission.PermissionListener
import com.example.webappboilerplate.activity.MainWithIntroActivity

class AndroidBridge(val activity: Activity, private val webView: BaseWebView<WindowActivity>) :
    BaseWebView.JavascriptBridgeFrame {

    @JavascriptInterface
    fun logIn(user_code: String) {
        Logger.log(Logger.LogType.INFO, "usercode : $user_code")
        Logger.log(Logger.LogType.INFO, "token_sequence : ${HttpConstantModel.token_sequence}")
        BasicTokenCallService.updateTokenWithUserCode(user_code) { result ->
            Logger.log(Logger.LogType.INFO, "updateTokenWithUserCode : ${result.toString()}")
        }
        if (HttpConstantModel.token_sequence == -1)
            FirebaseUtil.resetToken()
    }

    @JavascriptInterface
    fun logOut() {
        Logger.log(Logger.LogType.INFO, "logout")
    }

    @JavascriptInterface
    fun setPushAgreement(agreement: Boolean) {
        Logger.log(Logger.LogType.INFO, "setPushAgreement : $agreement")
        BasicTokenCallService.updateTokenWithAgreement(agreement) { result ->
            Logger.log(Logger.LogType.INFO, "setPushAgreement : ${result.toString()}")
        }
    }

    @JavascriptInterface
    fun requestPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                // Toast.makeText(activity, "Permission Granted", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                // Toast.makeText(activity, "Permission Denied\n$deniedPermissions", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.with(activity)
            .setPermissionListener(permissionListener)
            // .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.READ_CONTACTS
                , Manifest.permission.CALL_PHONE
                , Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.FOREGROUND_SERVICE
                , Manifest.permission.INTERNET
            )
            .check()
    }

    @JavascriptInterface
    fun kakaoLogin() {
        val it = Intent(activity, BasicKakaoActivity::class.java)
        it.putExtra(AbstractSocialActivity.ACTION_TYPE, AbstractSocialActivity.ActionType.LogIn)
        activity.startActivityForResult(it, AbstractKakaoActivity.REQUEST_CODE)
    }

    @JavascriptInterface
    fun naverLogin() {
        val it = Intent(activity, BasicNaverActivity::class.java)
        it.putExtra(AbstractSocialActivity.ACTION_TYPE, AbstractSocialActivity.ActionType.LogIn)
        activity.startActivityForResult(it, AbstractNaverActivity.REQUEST_CODE)
    }

    @JavascriptInterface
    fun confirmNaverLogin() {
        mainWebView?.run {
            post {
                loadUrl("javascript:confirmNaverSocialBind()")
            }
        }
    }

    @JavascriptInterface
    fun facebookLogin() {
        val it = Intent(activity, BasicFacebookActivity::class.java)
        it.putExtra(AbstractSocialActivity.ACTION_TYPE, AbstractSocialActivity.ActionType.LogIn)
        activity.startActivityForResult(it, AbstractFacebookActivity.REQUEST_CODE)
    }

    @JavascriptInterface
    fun paycoLogin() {
        val it = Intent(activity, BasicPaycoActivity::class.java)
        it.putExtra(AbstractSocialActivity.ACTION_TYPE, AbstractSocialActivity.ActionType.LogIn)
        activity.startActivityForResult(it, AbstractPaycoActivity.REQUEST_CODE)
    }

    @JavascriptInterface
//    fun joinFinish() {
    fun finishCallback() {
        Logger.log(Logger.LogType.INFO, "finishCallback")
        mainWebView?.post {
            mainWebView?.url?.let {
                Logger.log(Logger.LogType.INFO, "$it")
                activity.setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(BaseWebViewCommand.KEY_RESULT, true)
                    putExtra(BaseWebViewCommand.KEY_CALLBACK, UrlUtil.extractPathExceptQuery(it))
                })
                activity.finish()
            }
        }
    }

    @JavascriptInterface
    fun finish() {
        Logger.log(Logger.LogType.INFO, "Finish")
        if (activity !is MainWithIntroActivity)
            activity.finish()
    }

    @JavascriptInterface
    fun finishAndHome() {
        Logger.log(Logger.LogType.INFO, "FinishAndHome")
        webView?.post {
            webView.loadUrl("javascript:mm.loading.show()")
        }
        mainWebView?.post {
            mainWebView?.customWebViewClient?.addOnPageFinishCallback(object : BaseWebViewClient.OnPageFinishListener {
                override fun onPageFinish() {
                    finish()
                }
            })
            mainWebView?.home()
        }
    }

    @JavascriptInterface
    fun finishAndHomeWithMessage(message: String) {
        Logger.log(Logger.LogType.INFO, "finishAndHomeWithMessage")
        webView?.post {
            webView.loadUrl("javascript:mm.bom.alert(\"$message\", function() { cm.web.bridge.getAndroid().finishAndHome(); })")
        }
    }

    @JavascriptInterface
    fun agree() {
        Logger.log(Logger.LogType.INFO, "agree")
        webView.loadUrl("javascript:agree()")
    }

    @JavascriptInterface
    fun goBack() {
        Logger.log(Logger.LogType.INFO, "goBack")
        webView.loadUrl("javascript:goBack()")
    }
}