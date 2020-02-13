package com.dev.hare.socialloginmodule.activity.abstracts

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.net.URLEncoder

abstract class AbstractSocialActivity : AppCompatActivity() {
    companion object {
        const val ACTION_TYPE = "type"
        const val RESULT_CODE = 0x0010
        const val RESULT_CODE_SUCCESS = 0x0001
        const val RESULT_CODE_FAIL = 0x0002
    }

    enum class ActionType(private val value: String) {
        LogIn("logIn"),
        LogOut("logOut"),
        UnLink("unLink");

        fun getValue() = value
    }

    enum class Key(private val value: String) {
        AccessToken("accessToken"),
        RefreshToken("refreshToken"),
        Id("Id"),
        SocialType("SocialType"),
        RedirectUrl("RedirectUrl");

        fun getValue() = value
    }

    abstract val socialName: String
    protected var accessToken: String? = null
    protected var refreshToken: String? = null
    protected var loginId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (intent.getSerializableExtra(ACTION_TYPE)) {
            ActionType.LogIn -> logIn()
            ActionType.LogOut -> logOut()
            ActionType.UnLink -> unLink()
        }
    }

    protected fun returnMainActivity(accessToken: String?, refreshToken: String?, id: String?) {
        val kIntent = Intent()
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        this.loginId = id
        kIntent.putExtra(Key.AccessToken.getValue(), accessToken)
        kIntent.putExtra(Key.RefreshToken.getValue(), refreshToken)
        kIntent.putExtra(Key.Id.getValue(), id)
        kIntent.putExtra(Key.SocialType.getValue(), socialName)
        kIntent.putExtra(Key.RedirectUrl.getValue(), redirectUrl())
        setResult(RESULT_CODE_SUCCESS, kIntent)
        finish()
    }

    protected fun getRedirectUrlParam(): String {
        return "access_token=${URLEncoder.encode(accessToken, "UTF-8")}&refresh_token=${URLEncoder.encode(refreshToken, "UTF-8")}&loginId=$loginId"
    }

    abstract fun logIn()
    abstract fun logOut()
    abstract fun unLink()
    abstract fun redirectUrl(): String
}