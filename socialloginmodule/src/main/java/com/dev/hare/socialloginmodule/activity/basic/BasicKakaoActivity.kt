package com.dev.hare.socialloginmodule.activity.basic

import com.dev.hare.apputilitymodule.util.Logger
import com.dev.hare.socialloginmodule.activity.abstracts.AbstractKakaoActivity
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import java.util.*


open class BasicKakaoActivity : AbstractKakaoActivity() {
    override val logoutCallback: LogoutResponseCallback = object : LogoutResponseCallback() {
        override fun onCompleteLogout() {
        }
    }

    override val unLinkCallback: UnLinkResponseCallback = object : UnLinkResponseCallback() {
        override fun onFailure(errorResult: ErrorResult?) {
            Logger.log(Logger.LogType.ERROR, "onFailure : " + errorResult!!.errorMessage)
            finish()
        }

        override fun onSessionClosed(errorResult: ErrorResult) {
            Logger.log(Logger.LogType.INFO, "onSessionClosed : " + errorResult.errorMessage)
            finish()
        }

        override fun onNotSignedUp() {
            Logger.log(Logger.LogType.INFO, "not sign up")
            finish()
        }

        override fun onSuccess(userId: Long?) {
            Logger.log(Logger.LogType.INFO, "onSuccess")
            UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
                override fun onCompleteLogout() {
                    finish()
                }
            })
        }
    }

    override fun requestProfile() {
        val keys = ArrayList<String>()
        keys.add("properties.nickname")
        keys.add("properties.profile_image")
        keys.add("kakao_account.email")
        UserManagement.getInstance().me(keys, object : MeV2ResponseCallback() {
            override fun onSuccess(result: MeV2Response?) {
                result?.let {
                    // Logger.log(Logger.LogType.INFO, "${it.nickname}", "${it.id}", "${it.kakaoAccount.email}")
                    returnMainActivity(accessToken, refreshToken, "${it.id}")
                }
            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
                Logger.log(Logger.LogType.ERROR, "onSessionClosed", errorResult?.errorMessage + "")
                finish()
            }
        })
    }

    override fun redirectUrl(): String{
        return "/auth/social/kakao/callback?${getRedirectUrlParam()}"
    }
}
