package com.dev.hare.socialloginmodule.activity.abstracts

import android.content.Intent
import com.facebook.AccessToken.getCurrentAccessToken
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.util.*
import com.facebook.FacebookCallback as FacebookCallback1


abstract class AbstractFacebookActivity : AbstractSocialActivity() {
    companion object {
        const val REQUEST_CODE = 0x0012
    }

    override val socialName: String = "facebook"
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    protected abstract val loginCallback: FacebookCallback1<LoginResult>

    override fun logIn() {
        LoginManager.getInstance().registerCallback(callbackManager, loginCallback)
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
    }

    override fun logOut() {
        LoginManager.getInstance().logOut()
    }

    fun isLoggedIn(): Boolean {
        val accessToken = getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
