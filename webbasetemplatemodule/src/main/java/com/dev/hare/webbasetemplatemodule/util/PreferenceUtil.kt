package com.dev.hare.webbasetemplatemodule.util

import android.content.Context

object PreferenceUtil {
    private val SHARED_PREFERENCES_NAME = "WebBaseTemplatePreference"

    fun setData(context: Context, key: String, contentsCode: String): Boolean {
        val editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(key, contentsCode)
        return editor.commit()
    }

    fun getData(context: Context, key: String): String? {
        val savedSession = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return savedSession.getString(key, "") // "" : undifined, Y : yes, N :
        // no
    }

    fun isFirstInstall(context: Context): Boolean {
        val pref = context.getSharedPreferences("VER", 0)
        var version = 0
        var oldVer = 0
        try {
            val pm = context.packageManager
            val packageInfo = pm.getPackageInfo(context.packageName, 0)
            version = packageInfo.versionCode
            oldVer = pref.getInt("version", 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return oldVer < version
    }

    fun completeFirstInstall(context: Context) {
        val pref = context.getSharedPreferences("VER", 0)
        var version = 0
        var oldVer = 0
        try {
            val pm = context.packageManager
            val packageInfo = pm.getPackageInfo(context.packageName, 0)
            version = packageInfo.versionCode
            oldVer = pref.getInt("version", 0)
            if (oldVer < version) {
                val edit = pref.edit()
                edit.putInt("version", version)
                edit.commit()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}