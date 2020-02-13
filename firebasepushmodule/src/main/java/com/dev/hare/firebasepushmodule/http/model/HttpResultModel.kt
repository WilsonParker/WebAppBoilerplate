package com.dev.hare.firebasepushmodule.http.model

data class HttpResultModel(
    var code: String?,
    var success: String?,
    var message: String?,
    var result: String?,
    var data: Map<String, String>
)