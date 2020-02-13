package com.dev.hare.firebasepushmodule.basic

import com.dev.hare.firebasepushmodule.http.abstracts.AbstractTokenCallService

object BasicTokenCallService : AbstractTokenCallService() {
    override val key: String = ""
    override val baseUrl: String = "http://example.api.co.kr"
}
