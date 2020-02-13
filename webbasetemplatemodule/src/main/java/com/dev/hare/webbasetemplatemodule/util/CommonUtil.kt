package com.dev.hare.webbasetemplatemodule.util

import android.content.Context
import android.net.ConnectivityManager
import android.provider.Settings
import android.telephony.TelephonyManager
import java.io.UnsupportedEncodingException
import java.util.*

object CommonUtil {
    private const val _PROPERTY_DEVICE_ID = "DEVICE-ID"

    /**
     * 네트워크 상태 enum 상수
     */
    enum class NetworkState {
        NOT_CONNECTED, IS_ROAMING, IS_WIFI, IS_MOBILE, UNKNOWHOST
    }

    fun isNetworkState(context: Context): NetworkState {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                return NetworkState.IS_WIFI
            } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                return NetworkState.IS_MOBILE
            }
        }
        return NetworkState.NOT_CONNECTED
    }

    /**
     * 디바이스 고유 아이디 리턴 <br></br> 1. ANDROID_ID 가져옴 <br></br> 2. 특정 번호로만 나오는 버그에 대한 예외처리 <br></br> 3. ANDROID_ID 가
     * 없으면 getDeviceID()를 사용 <br></br> 4. 둘다 실패 하면 랜덤하게 UUID 발생 <br></br> 5. 4번은 랜덤한 값으로 preference에 저장 삭제 후
     * 재설치 시 초기화 <br></br> 2015. 8. 20., zcom@forbiz.co.kr
     *
     * @return
     */
    fun getDeviceUUID(context: Context): String {
        val id = PreferenceUtil.getData(context, _PROPERTY_DEVICE_ID)
        var uuid: UUID?
        if (id != null && "" != id) {
            uuid = UUID.fromString(id)
        } else {
            val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            try {
                if ("9774d56d682e549c" != androidId) {
                    uuid = UUID.nameUUIDFromBytes(androidId.toByteArray(charset("utf8")))
                } else {
                    val deviceId = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
                    uuid =
                        if (deviceId != null) UUID.nameUUIDFromBytes(deviceId.toByteArray(charset("utf8"))) else UUID.randomUUID()
                }
            } catch (e: UnsupportedEncodingException) {
                throw RuntimeException(e)
            }

            PreferenceUtil.setData(context, _PROPERTY_DEVICE_ID, uuid!!.toString())
        }

        return uuid!!.toString()
    }

}