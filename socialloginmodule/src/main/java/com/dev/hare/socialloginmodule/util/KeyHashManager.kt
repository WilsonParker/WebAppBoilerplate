package com.dev.hare.socialloginmodule.util

import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import com.kakao.util.helper.Utility.getPackageInfo
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


object KeyHashManager {

    fun getKeyHash(context: Context): String? {
        val packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES) ?: return null

        for (signature in packageInfo.signatures) {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP)
            } catch (e: NoSuchAlgorithmException) {
                Log.w(TAG, "Unable to get MessageDigest. signature=$signature", e)
            }

        }
        return null
    }

    fun getKeyHash2(context: Context): String? {
        val TAG = "KeyHash"
        var keyHash: String? = null
        try {
            val info = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                keyHash = String(Base64.encode(md.digest(), 0))
                Log.d(TAG, keyHash)
            }
        } catch (e: Exception) {
            Log.e(TAG, "name not found$e")
        }

        return keyHash
    }

    fun getKeyHashWithSha1(sha1: ByteArray): String {
        var example = byteArrayOf(
            0xFB.toByte(),
            0x7E.toByte(),
            0x27.toByte(),
            0xFE.toByte(),
            0x3E.toByte(),
            0x36.toByte(),
            0x11.toByte(),
            0x7D.toByte(),
            0x89.toByte(),
            0x2A.toByte(),
            0x6F.toByte(),
            0x5A.toByte(),
            0x9E.toByte(),
            0xB1.toByte(),
            0xA0.toByte(),
            0x9B.toByte(),
            0x21.toByte(),
            0x2B.toByte(),
            0x30.toByte(),
            0x05.toByte()
        )
        return Base64.encodeToString(sha1, Base64.NO_WRAP)
    }
}