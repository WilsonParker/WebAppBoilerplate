package com.dev.hare.webbasetemplatemodule.web

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.dev.hare.webbasetemplatemodule.util.UrlUtil
import com.dev.hare.webbasetemplatemodule.web.interfaces.WebViewCommand
import kotlin.reflect.KClass

open class BaseWebViewCommand<activity : Activity>(
    private val context: Context,
    val host: String,
    private val activityCls: KClass<activity>
) : WebViewCommand {
    companion object {
        const val REQUEST_CODE = 0x0100
        const val KEY_RESULT = "result"
        const val KEY_CALLBACK = "callback"
    }

    private val INTENT_PROTOCOL_START = "intent:"
    private val INTENT_PROTOCOL_INTENT = "#Intent;"
    private val INTENT_PROTOCOL_END = ";end;"
    private val GOOGLE_PLAY_STORE_PREFIX = "market://details?id="

    protected open val localNewWindow = arrayOf("")
    protected open val localNewWindowWithRequest = arrayOf("")
    protected open val localNewApplication = arrayOf("")
    protected open val outerNewApplication = arrayOf("")
    protected open val exceptionUri = arrayOf("social-plugins.line.me")

    open val callbackMap = mapOf<String, () -> Any>()

    /**
     * 새로운 창으로 띄울지 설정합니다
     *
     * @author     Hare
     * @added      2019-06-11
     * @updated    2019-06-11
     * */
    var openable: Boolean = true
    /**
     * Activity.startActivityForResult 를 실행합니다
     *
     * @author     Hare
     * @added      2019-06-11
     * @updated    2019-06-11
     * */
    var withRequest: Boolean = false

    /**
     * 새로운 Activity 로 띄울지 결정합니다
     *
     * @param      url: Uri
     * @return     Boolean
     * @author     Hare
     * @added      2019-06-04
     * @updated    2019-06-04
     * */
    override fun isNewWindow(url: Uri): Boolean {
        return isLocalNewWindow(url) || isLocalNewWindowWithRequest(url) || !UrlUtil.isCurrentHost(
            host,
            url.toString()
        ) || !UrlUtil.isCurrentDomain(host, url.toString())
    }

    /**
     * 내부 activity 로 띄울지 결정합니다
     *
     * @param      url: Uri
     * @return     Boolean
     * @author     Hare
     * @added      2019-06-04
     * @updated    2019-06-04
     * */
    override fun isNewApplication(url: Uri): Boolean {
        return url.toString().run {
            isLocalNewApplication(url) || startsWith(INTENT_PROTOCOL_START) || startsWith(GOOGLE_PLAY_STORE_PREFIX)
        }
    }

    /**
     * url 을 load 하는데 설정에 따라 window, application 로 실행합니다
     *
     * @param      url: Uri
     * @return     Boolean
     * @author     Hare
     * @added      2019-06-04
     * @updated    2019-06-04
     * */
    override fun load(url: Uri): Boolean {
        return if (isExceptionUri(url))
            false
        else if (isNewApplication(url) || isOuterNewApplication(url))
            newApplication(url)
        else if (openable && isNewWindow(url)) {
            if (isLocalNewWindowWithRequest(url))
                newWindowWithRequest(url)
            else
                newWindow(url)
        } else
            newApplication(url)
    }

    /**
     * 새로운 WindowActivity 를 실행합니다
     *
     * @param      url: Uri
     * @return     Boolean
     * @author     Hare
     * @added      2019-06-04
     * @updated    2019-06-04
     * */
    override fun newWindow(url: Uri): Boolean {
        // Logger.log(Logger.LogType.INFO, "${context is BaseWindowActivity}")
        // Logger.log(Logger.LogType.INFO, "${context is BaseMainActivity<*>}")
        context.startActivity(Intent(context, activityCls.java).apply { data = url })
        return true
    }

    /**
     * 새로운 WindowActivity 를 startActivityForResult 로 실행합니다
     *
     * @param      url: Uri
     * @return     Boolean
     * @author     Hare
     * @added      2019-06-11
     * @updated    2019-06-11
     * */
    override fun newWindowWithRequest(url: Uri): Boolean {
        (context as Activity).startActivityForResult(
            Intent(context, activityCls.java).apply { data = url },
            REQUEST_CODE
        )
        return true
    }

    /**
     * 내부 activity 를 실행합니다
     *
     * @param      url: Uri
     * @return     Boolean
     * @author     Hare
     * @added      2019-06-04
     * @updated    2019-06-05
     * */
    override fun newApplication(url: Uri): Boolean {
        return url.toString().run {
            when {
                startsWith(INTENT_PROTOCOL_START) -> {
                    val customUrlStartIndex = INTENT_PROTOCOL_START.length
                    val customUrlEndIndex = indexOf(INTENT_PROTOCOL_INTENT)
                    if (customUrlEndIndex < 0) {
                        false
                    } else {
                        val customUrl = substring(customUrlStartIndex, customUrlEndIndex)
                        try {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(customUrl)))
                        } catch (e: ActivityNotFoundException) {
                            val packageStartIndex = customUrlEndIndex + INTENT_PROTOCOL_INTENT.length
                            val packageEndIndex = indexOf(INTENT_PROTOCOL_END)

                            val packageName =
                                substring(packageStartIndex, if (packageEndIndex < 0) length else packageEndIndex)
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(GOOGLE_PLAY_STORE_PREFIX + packageName)
                                )
                            )
                        }
                        true
                    }
                }
                isLocalNewApplication(this) -> {
                    localNewApplication(this)
                }
                else -> {
                    basicNewApplication(this)
                }
            }
        }
    }

    /**
     * 내부 url 중 application 으로 띄울지 결정합니다
     *
     * @param      url
     * @return     Boolean
     * @author     Hare
     * @added      2019-06-11
     * @updated    2019-06-11
     * */
    protected fun localNewApplication(url: String): Boolean {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
        )
        return true
    }

    protected fun localNewApplication(url: Uri): Boolean {
        return localNewApplication(url.toString())
    }

    /**
     * newApplication(Uri) 에서 마지막 조건으로 실행합니다
     *
     * @param      url: Uri
     * @return     Boolean
     * @author     Hare
     * @added      2019-06-11
     * @updated    2019-06-11
     * */
    protected open fun basicNewApplication(url: String): Boolean {
        return !UrlUtil.isCurrentHost(host, url)
    }

    protected open fun basicNewApplication(url: Uri): Boolean {
        return basicNewApplication(url.toString())
    }

    /**
     * 내부 url 중 WindowActivity 로 띄울지 결정합니다
     *
     * @param      url: Uri
     * @return     Boolean
     * @author     Hare
     * @added      2019-06-04
     * @updated    2019-06-05
     * */
    protected open fun isLocalNewWindow(url: String): Boolean {
        return isContainsUrl(url, localNewWindow)
    }

    protected open fun isLocalNewWindow(url: Uri): Boolean {
        return isLocalNewWindow(url.toString())
    }

    /**
     * 새로운 창으로 띄우면서 Result 를 반환할지 결정합니다
     *
     * @param      url: Uri
     * @return     Boolean
     * @author     Hare
     * @added      2019-06-11
     * @updated    2019-06-11
     * */
    protected fun isLocalNewWindowWithRequest(url: String): Boolean {
        return isContainsUrl(url, localNewWindowWithRequest)
    }

    protected fun isLocalNewWindowWithRequest(url: Uri): Boolean {
        return isLocalNewWindowWithRequest(url.toString())
    }

    /**
     * 내부 url 중 새로운 Application 으로 띄울지 결정합니다
     *
     * @param      url: Uri
     * @return     Boolean
     * @author     Hare
     * @added      2019-06-05
     * @updated    2019-06-05
     * */
    protected fun isLocalNewApplication(url: String): Boolean {
        return isContainsUrl(url, localNewApplication)
    }

    protected fun isLocalNewApplication(url: Uri): Boolean {
        return isLocalNewApplication(url.toString())
    }

    /**
     * 외부 url 중 새로운 Application 으로 띄울지 결정합니다
     *
     * @param      url: Uri
     * @return     Boolean
     * @author     Hare
     * @added      2019-06-11
     * @updated    2019-06-11
     * */
    protected fun isOuterNewApplication(url: String): Boolean {
        return isContainsUrl(url, outerNewApplication)
    }

    protected fun isOuterNewApplication(url: Uri): Boolean {
        return isOuterNewApplication(url.toString())
    }

    /**
     * 별도 action 을 취하지 않을 지 결정합니다
     *
     * @param      url: Uri
     * @return     Boolean
     * @author     Hare
     * @added      2019-06-25
     * @updated    2019-06-25
     * */
    protected fun isExceptionUri(url: Uri): Boolean {
        return isExceptionUri(url.toString())
    }

    protected fun isExceptionUri(url: String): Boolean {
        return isContainsUrl(url, exceptionUri)
    }

    /**
     * list 에 있는 내용이 url 에 포함되는지 확인합니다
     *
     * @param      url: Uri
     * @param      list: Array<String>
     * @return     Boolean
     * @author     Hare
     * @added      2019-06-11
     * @updated    2019-06-11
     * */
    protected fun isContainsUrl(url: Uri, list: Array<String>): Boolean {
        return isContainsUrl(url.toString(), list)
    }

    protected fun isContainsUrl(url: String, list: Array<String>): Boolean {
        list.forEach { local ->
            if (local.isNotEmpty() && url.contains(local)) {
                return true
            }
        }
        return false
    }

    /**
     * 내부 url 중 새로운 Application 으로 띄울지 결정합니다
     *
     * @param      key: String
     * @author     Hare
     * @added      2019-06-05
     * @updated    2019-06-05
     * */
    fun callResultFunction(key: String) {
        callbackMap[key]?.let { it() }
    }
}