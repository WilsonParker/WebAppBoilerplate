package com.example.webappboilerplate.social

import com.dev.hare.socialloginmodule.activity.basic.BasicNaverActivity

class BasicNaverActivity : BasicNaverActivity() {

    override fun redirectUrl(): String{
         return "/auth/social/naver/token-to-login?${getRedirectUrlParam()}"
    }
}