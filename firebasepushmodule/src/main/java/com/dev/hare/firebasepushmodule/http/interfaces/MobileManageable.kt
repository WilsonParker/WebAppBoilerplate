package com.dev.hare.firebasepushmodule.http.interfaces

import com.dev.hare.firebasepushmodule.http.model.HttpResultModel
import retrofit2.Call
import retrofit2.http.GET


interface MobileManageable{

    @GET("/mobile/getIntroImageUrl")
    fun getIntroImageUrl(): Call<HttpResultModel>

}