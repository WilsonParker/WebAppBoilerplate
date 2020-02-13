package com.example.webappboilerplate.push

import com.dev.hare.firebasepushmodule.http.abstracts.AbstractTokenCallService
import com.example.webappboilerplate.data.API_KEY
import com.example.webappboilerplate.data.APP_API_URL

object BasicTokenCallService : AbstractTokenCallService() {
    override val key: String = API_KEY
    override val baseUrl: String
        get() = APP_API_URL
}
