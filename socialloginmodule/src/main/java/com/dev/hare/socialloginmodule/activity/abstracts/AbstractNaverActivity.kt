package com.dev.hare.socialloginmodule.activity.abstracts

import android.widget.Toast
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler


abstract class AbstractNaverActivity : AbstractSocialActivity() {
    companion object {
        const val REQUEST_CODE = 0x0013
    }

    override val socialName: String = "naver"
    protected val mOAuthLoginInstance: OAuthLogin = OAuthLogin.getInstance()

    /**
     * OAuthLoginHandler를 startOAuthLoginActivity() 메서드 호출 시 파라미터로 전달하거나 OAuthLoginButton 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다.
     */
    private val mOAuthLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            if (success) {
                var accessToken = mOAuthLoginInstance.getAccessToken(baseContext)
                var refreshToken = mOAuthLoginInstance.getRefreshToken(baseContext)
                var expiresAt = mOAuthLoginInstance.getExpiresAt(baseContext)
                var tokenType = mOAuthLoginInstance.getTokenType(baseContext)
                var state = mOAuthLoginInstance.getState(baseContext)
                returnMainActivity(accessToken, refreshToken, "")
            } else {
                var errorCode = mOAuthLoginInstance.getLastErrorCode(baseContext).code
                var errorDesc = mOAuthLoginInstance.getLastErrorDesc(baseContext)
                Toast.makeText(baseContext, "errorCode:$errorCode, errorDesc:$errorDesc", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun logIn() {
        mOAuthLoginInstance.startOauthLoginActivity(this, mOAuthLoginHandler)
    }

    override fun logOut() {
        mOAuthLoginInstance.logout(baseContext)
    }
}