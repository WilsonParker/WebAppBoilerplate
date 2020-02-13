package com.example.webappboilerplate.social

import com.dev.hare.socialloginmodule.activity.basic.BasicFacebookActivity


class BasicFacebookActivity : BasicFacebookActivity() {

    override fun redirectUrl(): String{
        return "/auth/social/facebook/token-to-login?${getRedirectUrlParam()}"
    }
}
