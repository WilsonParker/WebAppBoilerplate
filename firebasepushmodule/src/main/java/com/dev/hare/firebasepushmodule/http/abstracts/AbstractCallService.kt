package com.dev.hare.firebasepushmodule.http.abstracts

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass

abstract class AbstractCallService<service : Any> {
    abstract val baseUrl: String
    protected abstract val retrofitCallableClass: KClass<service>
    protected val retrofit: Retrofit = Retrofit.Builder().apply {
        baseUrl(baseUrl)
        // client(OkHttpClient())
        addConverterFactory(GsonConverterFactory.create())
    }.build()
    protected val pushService: service = retrofit.create(retrofitCallableClass.java)

}