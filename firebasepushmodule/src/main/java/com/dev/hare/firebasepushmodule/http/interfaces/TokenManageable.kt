package com.dev.hare.firebasepushmodule.http.interfaces

import com.dev.hare.firebasepushmodule.http.model.HttpResultModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface TokenManageable {

    @GET("/mobile/insertToken")
    fun insertToken(@Query("token") token: String, @Query("os_type") os_type: String, @Query("device_id") device_id: String, @Query("api_key") api_key: String): Call<HttpResultModel>

    @GET("/mobile/updateTokenWithUserCode")
    fun updateTokenWithUserCode(@Query("sequence") sequence: Int, @Query("user_code") user_code: String, @Query("api_key") api_key: String): Call<HttpResultModel>

    @GET("/mobile/updateTokenWithUser")
    fun updateTokenWithUser(@Query("sequence") sequence: Int, @Query("api_key") key: String): Call<HttpResultModel>

    @GET("/mobile/updateTokenWithAgreement")
    fun updateTokenWithAgreement(@Query("sequence") sequence: Int, @Query("agreement") agreement: Boolean, @Query("api_key") api_key: String): Call<HttpResultModel>
}