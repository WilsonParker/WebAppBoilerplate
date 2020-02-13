package com.dev.hare.firebasepushmodule.util

import com.dev.hare.apputilitymodule.util.Logger
import com.dev.hare.apputilitymodule.util.file.PreferenceUtil
import com.google.firebase.iid.FirebaseInstanceId

object FirebaseUtil {
    private const val PrefToken = "token"
    private const val PrefTokenSequence = "sequence"

    fun getToken(getTokenSuccessListener: OnGetTokenSuccessListener) {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            Logger.log(Logger.LogType.INFO, "token = ${it.token}")
            getTokenSuccessListener.onSuccess(it.token)
        }
    }

    fun getTokenSequence(): Int {
        var sequence = PreferenceUtil.getData<Int>(PrefTokenSequence, Int.javaClass)
        return sequence
    }

    fun setTokenSequence(sequence: Int) {
        PreferenceUtil.setData(PrefTokenSequence, sequence)
    }

    fun isTokenRestored(token: String): Boolean {
        var prefToken = PreferenceUtil.getData(PrefToken)
        return if (prefToken == "" || prefToken != token) {
            PreferenceUtil.setData(PrefToken, token)
            true
        } else {
            false
        }
    }

    fun resetToken() {
        PreferenceUtil.setData(PrefToken, "")
        PreferenceUtil.setData(PrefTokenSequence, -1)
    }


    interface OnGetTokenSuccessListener {
        fun onSuccess(token: String)
    }
}