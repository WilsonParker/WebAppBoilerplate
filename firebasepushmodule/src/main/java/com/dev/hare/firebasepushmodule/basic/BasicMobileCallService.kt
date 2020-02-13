package com.dev.hare.firebasepushmodule.basic

import com.dev.hare.firebasepushmodule.http.abstracts.AbstractMobileCallService

object BasicMobileCallService : AbstractMobileCallService() {
    override val baseUrl: String = "http://example.api.co.kr"
}