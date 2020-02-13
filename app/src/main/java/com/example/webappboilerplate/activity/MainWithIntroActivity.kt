package com.example.webappboilerplate.activity

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import com.dev.hare.firebasepushmodule.http.model.HttpConstantModel
import com.dev.hare.firebasepushmodule.util.FirebaseUtil
import com.dev.hare.apputilitymodule.util.Logger
import com.dev.hare.socialloginmodule.activity.abstracts.AbstractSocialActivity
import com.dev.hare.webbasetemplatemodule.activity.BaseMainWithIntroImageActivity
import com.dev.hare.webbasetemplatemodule.activity.BaseWindowActivity
import com.dev.hare.webbasetemplatemodule.util.CommonUtil
import com.dev.hare.webbasetemplatemodule.web.BaseWebViewCommand
import kotlinx.android.synthetic.main.activity_main.*
import com.example.webappboilerplate.R
import com.example.webappboilerplate.data.APP_URL
import com.example.webappboilerplate.data.IMG_URL
import com.example.webappboilerplate.push.BasicMobileCallService
import com.example.webappboilerplate.push.BasicTokenCallService
import com.example.webappboilerplate.util.NicePayUtility
import com.example.webappboilerplate.web.AndroidBridge
import com.example.webappboilerplate.web.CustomWebView
import kotlin.reflect.KClass

/**
 * Main 위에 intro image 를 띄우는 Activity 입니다
 *
 * @author      Hare
 * @added       2019-06-27
 * @updated     2019-06-27
 * */
class MainWithIntroActivity : BaseMainWithIntroImageActivity() {
    override val windowActivity: KClass<BaseWindowActivity> = WindowActivity::class as KClass<BaseWindowActivity>
    override val introImageID
        get() = R.drawable.intro
    override val introImageView: ImageView
        get() = intro_image
    override val mainView: ViewGroup
        get() = Layout_main
    override val splashTimeOut: Long
        get() = 2500

    companion object {
        var mainWebView: CustomWebView? = null
    }

    override fun init() {
        BasicMobileCallService.getIntroImageUrl {
            Logger.log(Logger.LogType.INFO, "getIntroImageUrl : ${it.toString()}")
            loadIntro(IMG_URL + it?.data?.get("url").toString())
        }
    }

    override fun onCreateInit(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        mainWebView = webview
    }

    override fun onCreateAfter(savedInstanceState: Bundle?) {
        FirebaseUtil.getToken(object : FirebaseUtil.OnGetTokenSuccessListener {
            override fun onSuccess(token: String) {
                if (FirebaseUtil.isTokenRestored(token)) {
                    BasicTokenCallService.insertToken(
                        this@MainWithIntroActivity,
                        token,
                        CommonUtil.getDeviceUUID(this@MainWithIntroActivity)
                    ) {
                        Logger.log(Logger.LogType.INFO, "token sequence : ${it?.toString()}")
                    }
                } else {
                    HttpConstantModel.token_sequence = FirebaseUtil.getTokenSequence()
                    Logger.log(Logger.LogType.INFO, "token is not restored : ${HttpConstantModel.token_sequence}")
                    if (HttpConstantModel.token_sequence == -1) {
                        FirebaseUtil.resetToken()
                    }
                }
            }
        })
        // Logger.log(Logger.LogType.INFO, "keyhash", "" + KeyHashManager.getKeyHash(this))

        webview.host = getLink(APP_URL)
        webview.javascriptBrideInterface = AndroidBridge(this, webview)
        webview.initWebView(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Logger.log(Logger.LogType.INFO, "activity result ${requestCode} ${resultCode}")

        when {
            requestCode == NicePayUtility.REQUEST_CODE -> {
                data?.let {
                    NicePayUtility.onActivityResult(requestCode, resultCode, it, webview, this)
                }
            }

            requestCode == BaseWebViewCommand.REQUEST_CODE -> {
                Logger.log(Logger.LogType.INFO, "BaseWebViewCommand.KEY_CALLBACK")
                Logger.log(Logger.LogType.INFO, "${data?.getStringExtra(BaseWebViewCommand.KEY_CALLBACK)}")
                data?.getStringExtra(BaseWebViewCommand.KEY_CALLBACK)?.let {
                    webview.webViewCommand?.callResultFunction(it)
                }
            }

            (requestCode or AbstractSocialActivity.RESULT_CODE) >= AbstractSocialActivity.RESULT_CODE -> {
                if (resultCode == AbstractSocialActivity.RESULT_CODE_SUCCESS || resultCode == 0) {
                    data?.let {
                        var accessToken = data.getStringExtra(AbstractSocialActivity.Key.AccessToken.getValue())
                        var refreshToken = data.getStringExtra(AbstractSocialActivity.Key.RefreshToken.getValue())
                        var id = data.getStringExtra(AbstractSocialActivity.Key.Id.getValue())
                        var redirectUrl = data.getStringExtra(AbstractSocialActivity.Key.RedirectUrl.getValue())
                        val url = "${webview.host}$redirectUrl"
                        Logger.log(Logger.LogType.INFO, accessToken, refreshToken, id, url)
                        webview.post {
                            // webview.loadUrl(url)
                            webview.loadUrl("javascript:mm.link(\"$url\")")
                        }
                    }
                }
            }
        }
    }

}
