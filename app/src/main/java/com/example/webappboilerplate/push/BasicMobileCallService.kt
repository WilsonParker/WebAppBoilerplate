package com.example.webappboilerplate.push

import com.dev.hare.firebasepushmodule.http.abstracts.AbstractMobileCallService
import com.example.webappboilerplate.data.ADMIN_API_URL

object BasicMobileCallService : AbstractMobileCallService() {
    override val baseUrl: String
        get() = ADMIN_API_URL
}