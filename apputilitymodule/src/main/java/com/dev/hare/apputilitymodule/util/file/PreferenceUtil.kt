package com.dev.hare.apputilitymodule.util.file

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.dev.hare.apputilitymodule.data.abstracts.Initializable
import com.dev.hare.apputilitymodule.util.file.PreferenceUtil.Key.IS_FIRST

object PreferenceUtil : Initializable {
    private var SHARED_PREFERENCES_NAME = "PreferenceUtil"
    private var preference: SharedPreferences? = null

    override fun initialize(context: Context) {
        this.preference = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    }

    internal object Key {
        val VERSION = "ver"
        val OLD_VERSION = "old_ver"
        val IS_FIRST = "isfirst"
        val AUTO_LOGIN= "autoLogin"
    }

    fun setPreferenceName(name: String) {
        SHARED_PREFERENCES_NAME = name
    }

    fun <Val : Any> setData(context: Context, key: String, value: Val): Boolean {
        initialize(context)
        return setData(key, value)
    }

    fun <Val : Any> setData(key: String, value: Val): Boolean {
        var editor = preference?.edit()
        when (value) {
            is String -> editor?.putString(key, value)
            is Int -> editor?.putInt(key, value)
            is Long -> editor?.putLong(key, value)
            is Boolean -> editor?.putBoolean(key, value)
            is Float -> editor?.putFloat(key, value)
            else -> editor?.putString(key, value as String)
        }
        return editor?.commit() ?: false
    }

    fun getData(key: String): String {
        return getData(key, String.javaClass)
    }

    fun getData(context: Context, key: String): String {
        initialize(context)
        return getData(key, String.javaClass)
    }

    fun <Cast : Any> getData(context: Context, key: String, type: Class<*>): Cast {
        initialize(context)
        return getData(key, type)
    }

    fun <Cast : Any> getData(key: String, type: Class<*>): Cast {
        return when (type) {
            String.javaClass -> preference?.getString(key, "")
            Int.javaClass -> preference?.getInt(key, -1)
            Long.javaClass -> preference?.getLong(key, -1)
            Boolean.javaClass -> preference?.getBoolean(key, false)
            Float.javaClass -> preference?.getFloat(key, -1F)
            else -> preference?.getString(key, "")
        } as Cast
    }

    fun isFirstInstall(context: Context): Boolean {
        val pref = context.getSharedPreferences(Key.VERSION, MODE_PRIVATE)
        var version = 0
        var oldVer = 0
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = packageInfo.versionCode
            oldVer = pref.getInt(Key.OLD_VERSION, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return oldVer < version
    }

    fun isFirst(context: Context): Boolean {
        return getData(context, IS_FIRST) == ""
    }

    fun setFirstInstalled(context: Context): Boolean {
        return setData(context, IS_FIRST, "true")
    }

    fun completeFirstInstall(context: Context) {
        val pref = context.getSharedPreferences(Key.VERSION, MODE_PRIVATE)
        var version: Int
        var oldVer: Int
        try {
            val pm = context.packageManager
            val packageInfo = pm.getPackageInfo(context.packageName, 0)
            version = packageInfo.versionCode
            oldVer = pref.getInt(Key.OLD_VERSION, 0)
            if (oldVer < version) {
                val edit = pref.edit()
                edit.putInt(Key.OLD_VERSION, version)
                edit.commit()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}