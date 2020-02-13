package com.dev.hare.firebasepushmodule.http.abstracts

import android.content.Context
import com.dev.hare.apputilitymodule.util.Logger
import com.dev.hare.firebasepushmodule.http.interfaces.TokenManageable
import com.dev.hare.firebasepushmodule.http.model.HttpConstantModel
import com.dev.hare.firebasepushmodule.http.model.HttpResultModel
import com.dev.hare.firebasepushmodule.util.FirebaseUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.reflect.KClass

abstract class AbstractTokenCallService : AbstractCallService<TokenManageable>() {
    protected abstract val key: String
    override val retrofitCallableClass: KClass<TokenManageable>
        get() = TokenManageable::class

    fun insertToken(context: Context, token: String, device_id: String, callback: (result: HttpResultModel?) -> Unit) {
        pushService.insertToken(token, "a", device_id, key).enqueue(object : Callback<HttpResultModel> {
            override fun onResponse(call: Call<HttpResultModel>, response: Response<HttpResultModel>) {
                var result: HttpResultModel? = response.body()
                if (response.isSuccessful) {
                    try {
                        var sequence = Integer.parseInt(result?.data?.get("sequence"))
                        HttpConstantModel.token_sequence = sequence
                        FirebaseUtil.setTokenSequence(sequence)
                        callback(result)
                    } catch (e: NumberFormatException) {
                        Logger.log(Logger.LogType.ERROR, "insertToken", e)
                    }
                } else {
                    Logger.log(Logger.LogType.ERROR, "insertToken is not successful", response.message(), response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<HttpResultModel>, t: Throwable) {
                Logger.log(Logger.LogType.ERROR, "insertToken", t)
            }
        })
    }

    fun updateTokenWithUserCode(user_code: String, callback: (result: HttpResultModel?) -> Unit) {
        pushService.updateTokenWithUserCode(HttpConstantModel.token_sequence, user_code, key)
            .enqueue(object : Callback<HttpResultModel> {
                override fun onResponse(call: Call<HttpResultModel>, response: Response<HttpResultModel>) {
                    var result: HttpResultModel? = response.body()
                    if (response.isSuccessful) {
                        callback(result)
                    } else {
                        Logger.log(Logger.LogType.ERROR, "updateTokenWithUserCode is not successful", response.message(), response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<HttpResultModel>, t: Throwable) {
                    Logger.log(Logger.LogType.ERROR, "updateTokenWithUserCode", t)
                }
            })
    }

    fun updateTokenWithUser(callback: (result: HttpResultModel?) -> Unit) {
        pushService.updateTokenWithUser(HttpConstantModel.token_sequence, key)
            .enqueue(object : Callback<HttpResultModel> {
                override fun onResponse(call: Call<HttpResultModel>, response: Response<HttpResultModel>) {
                    var result: HttpResultModel? = response.body()
                    if (response.isSuccessful) {
                        callback(result)
                    } else {
                        Logger.log(Logger.LogType.ERROR, "updateTokenWithUser is not successful", response.message(), response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<HttpResultModel>, t: Throwable) {
                    Logger.log(Logger.LogType.ERROR, "updateTokenWithUser", t)
                }
            })
    }

    fun updateTokenWithAgreement(agreement: Boolean, callback: (result: HttpResultModel?) -> Unit) {
        pushService.updateTokenWithAgreement(HttpConstantModel.token_sequence, agreement, key)
            .enqueue(object : Callback<HttpResultModel> {
                override fun onResponse(call: Call<HttpResultModel>, response: Response<HttpResultModel>) {
                    var result: HttpResultModel? = response.body()
                    if (response.isSuccessful) {
                        callback(result)
                    } else {
                        Logger.log(Logger.LogType.ERROR, "updateTokenWithAgreement is not successful", response.message(), response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<HttpResultModel>, t: Throwable) {
                    Logger.log(Logger.LogType.ERROR, "updateTokenWithAgreement", t)
                }
            })
    }

}
