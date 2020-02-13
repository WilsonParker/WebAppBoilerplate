package com.dev.hare.apputilitymodule.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.dev.hare.apputilitymodule.util.Logger
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object VersionChecker {

    /**
     * Gets market version.
     *
     * @param packageName the package name
     * @return the market version
     */
    fun getMarketVersion(packageName: String): String? {
        try {
            val doc = Jsoup.connect("https://play.google.com/store/apps/details?id=$packageName").get()
            val Version = doc.select(".content")

            for (mElement in Version) {
                if (mElement.attr("itemprop") == "softwareVersion") {
                    return mElement.text().trim { it <= ' ' }
                }
            }

        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return null
    }

    /**
     * Gets market version fast result.
     *
     * @param packageName the package name
     * @return the market version fast result
     */
    fun getMarketVersionFast(packageName: String): String? {
        var mData = ""
        var mVer: String? = null

        try {
            val mUrl = URL("https://play.google.com/store/apps/details?id=$packageName")
            val mConnection = mUrl.openConnection() as HttpURLConnection ?: return null

            mConnection.connectTimeout = 5000
            mConnection.useCaches = false
            mConnection.doOutput = true

            if (mConnection.responseCode == HttpURLConnection.HTTP_OK) {
                val mReader = BufferedReader(InputStreamReader(mConnection.inputStream))

                while (true) {
                    val line = mReader.readLine() ?: break
                    mData += line
                }

                mReader.close()
            }

            mConnection.disconnect()

        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }

        val startToken = "softwareVersion\">"
        val endToken = "<"
        val index = mData.indexOf(startToken)

        if (index == -1) {
            mVer = null

        } else {
            mVer = mData.substring(index + startToken.length, index + startToken.length + 100)
            mVer = mVer.substring(0, mVer.indexOf(endToken)).trim { it <= ' ' }
        }

        return mVer
    }

    fun getVersionInfo(context: Context?): String {
        var version = "Unknown"
        var packageInfo: PackageInfo?

        if (context == null) {
            return version
        }
        try {
            packageInfo = context?.applicationContext
                .packageManager
                .getPackageInfo(context?.applicationContext.packageName, 0)
            version = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Logger.log(Logger.LogType.INFO, "getVersionInfo :" + e.message)
        }
        return version
    }
}