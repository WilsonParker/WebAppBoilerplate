package com.dev.hare.socialloginmodule.application

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.dev.hare.socialloginmodule.adapter.KakaoSDKAdapter
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.kakao.auth.KakaoSDK
import com.nhn.android.naverlogin.OAuthLogin
import com.toast.android.paycologin.EnvType
import com.toast.android.paycologin.PaycoLoginManager
import com.toast.android.paycologin.PaycoLoginManagerConfiguration


open class GlobalApplication : MultiDexApplication() {
    companion object {
        private var instance: GlobalApplication? = null
        fun getGlobalApplicationContext(): GlobalApplication {
            if (instance == null) {
                throw IllegalStateException("This Application does not inherit com.kakao.GlobalApplication")
            }
            return instance!!
        }
    }

    private lateinit var bundle: Bundle

    override fun onCreate() {
        super.onCreate()
        instance = this

        bundle = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).metaData
        kakaoInit()
        naverInit()
        facebookInit()
        paycoInit()
    }

    private fun kakaoInit() {
        KakaoSDK.init(KakaoSDKAdapter())
    }

    private fun naverInit() {
        bundle.let {
            OAuthLogin.getInstance().init(
                this,
                it.getString("NAVER_OAUTH_CLIENT_ID"),
                it.getString("NAVER_OAUTH_CLIENT_SECRET"),
                it.getString("NAVER_OAUTH_CLIENT_NAME")
            )
        }
    }

    private fun facebookInit() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    private fun paycoInit() {
        bundle.let {
            var stringId = applicationInfo.labelRes
            var appName = if (stringId == 0) applicationInfo.nonLocalizedLabel else getString(stringId)

            val configuration = PaycoLoginManagerConfiguration.Builder()
                .setServiceProviderCode("FRIENDS")
                .setClientId(it.getString("PAYCO_CLIENT_ID"))
                .setClientSecret(it.getString("PAYCO_SECRET_KEY"))
                .setAppName(appName.toString())
                .setEnvType(EnvType.REAL)
                //.setLangType(LangType.KOREAN)
                .build()

            PaycoLoginManager.getInstance().init(this, configuration)
        }

    }
}