package com.example.webappboilerplate.push

import com.dev.hare.firebasepushmodule.http.abstracts.AbstractCallService
import com.dev.hare.firebasepushmodule.http.model.HttpResultModel
import com.dev.hare.apputilitymodule.util.Logger
import com.example.webappboilerplate.data.API_KEY
import com.example.webappboilerplate.data.APP_API_URL
import com.example.webappboilerplate.push.interfaces.DownloadHistoryManageable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.reflect.KClass

object BasicHistoryCallService : AbstractCallService<DownloadHistoryManageable>() {
    val key: String = API_KEY
    override val retrofitCallableClass: KClass<DownloadHistoryManageable>
        get() = DownloadHistoryManageable::class
    override val baseUrl: String
        get() = APP_API_URL

    fun insertDownloadHistory(callback: (result: HttpResultModel?) -> Unit) {
        pushService.insertDownloadHistory("a", key).enqueue(object : Callback<HttpResultModel> {
            override fun onResponse(call: Call<HttpResultModel>, response: Response<HttpResultModel>) {
                var result: HttpResultModel? = response.body()
                if (response.isSuccessful) {
                    callback(result)
                } else {
                    Logger.log(
                        Logger.LogType.ERROR,
                        "insertDownloadHistory is not successful",
                        response.message(),
                        response.errorBody()!!.string()
                    )
                }
            }

            override fun onFailure(call: Call<HttpResultModel>, t: Throwable) {
                Logger.log(Logger.LogType.ERROR, "insertDownloadHistory", t)
            }
        })
    }
}
