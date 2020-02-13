package com.dev.hare.socialloginmodule.activity.abstracts

import android.content.Intent
import com.dev.hare.apputilitymodule.util.Logger
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import com.kakao.util.exception.KakaoException


abstract class AbstractKakaoActivity : AbstractSocialActivity() {
    companion object {
        const val REQUEST_CODE = 0x0011
    }

    override val socialName: String = "kakao"
    protected abstract val logoutCallback: LogoutResponseCallback
    protected abstract val unLinkCallback: UnLinkResponseCallback

    private var callback: SessionCallback? = null
    private var session: Session = Session.getCurrentSession()

    override fun logIn() {
        session = Session.getCurrentSession()
        if (session.isOpened)
            session.close()
        callback = SessionCallback()
        session.addCallback(callback)
        session.checkAndImplicitOpen()
        session.open(AuthType.KAKAO_TALK, this)
    }

    override fun logOut() {
        UserManagement.getInstance().requestLogout(logoutCallback)
    }

    override fun unLink() {
        UserManagement.getInstance().requestUnlink(unLinkCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Logger.log(Logger.LogType.INFO, "KaKaoLogin onActivityResult")
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }

    abstract fun requestProfile()

    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            val session = Session.getCurrentSession()
            accessToken = session.tokenInfo.accessToken
            refreshToken = session.tokenInfo.refreshToken
            requestProfile()
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Logger.log(Logger.LogType.ERROR, "onSessionOpenFailed", exception.message + "")
                finish()
            }
        }
    }

}
