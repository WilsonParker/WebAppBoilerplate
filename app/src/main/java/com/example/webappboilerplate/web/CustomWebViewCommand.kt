package com.example.webappboilerplate.web

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import com.dev.hare.apputilitymodule.util.Logger
import com.dev.hare.webbasetemplatemodule.util.UrlUtil
import com.dev.hare.webbasetemplatemodule.web.BaseWebViewCommand
import com.example.webappboilerplate.data.APP_URL
import com.example.webappboilerplate.util.NicePay.WebViewActivity
import kotlin.reflect.KClass


class CustomWebViewCommand<activity : Activity>(
    private val context: Context,
    host: String,
    activityCls: KClass<activity>,
    private val webView: WebView
) : BaseWebViewCommand<activity>(
    context,
    host,
    activityCls
) {

    override val callbackMap = mapOf<String, () -> Any>(
        "/auth/regist/certificate" to {
            webView.loadUrl("javascript:\$(\"[src*='/auth/regist/certificate']\")[0].contentWindow.agree();")
        },
        "/auth/search/idsearch" to {
            webView.loadUrl("javascript:\$(\"[src*='/auth/search/idsearch']\")[0].contentWindow.agree();")
        },
        "/auth/search/pwsearch" to {
            webView.loadUrl("javascript:\$(\"[src*='/auth/search/pwsearch']\")[0].contentWindow.agree();")
        }
    )

    private val localBasicNewApplication = arrayOf("/order/process")

    override val localNewWindow = arrayOf(
        // 상품 자세히 보기
        "product/detail"
        // 소셜 로그인
        , "/auth/social"
        // 스마트폰 본인 인증
        // 결제
        , "/order/process"
    )
    override val localNewWindowWithRequest = arrayOf(
        // 스마트폰 본인 인증
        "common/identifyUser"
    )

    override val outerNewApplication = arrayOf(
        "nice.checkplus.co.kr/CheckPlusSafeModel/checkplus.cb"
        // NicePay 결제 취소
        , "web.nicepay.co.kr/v3/smart/common/error"
        // 네이버 로그인
        , "nid.naver.com"
    )

    override fun load(url: Uri): Boolean {
        return if (url.toString().contains("/order/process")){
            var intent = Intent(context, WebViewActivity::class.java).apply {
                data = url
            }
            context.startActivity(intent)
            return true
        } else super.load(url)
    }

    override fun isNewWindow(url: Uri): Boolean {
        return when {
            !UrlUtil.isCurrentDomain(APP_URL, url.toString()) -> {
                false
            }
            !UrlUtil.isCurrentDomain(host, url.toString()) -> {
                !isOuterNewApplication(url)
            }
            isLocalNewWindowWithRequest(url) || isLocalNewWindow(url) -> {
                true
            }
            UrlUtil.isCurrentDomain(host, url.toString()) -> {
                false
            }
            UrlUtil.isCurrentDomain(APP_URL, host) -> {
                if (UrlUtil.isCurrentDomain(host, url.toString()))
                    isLocalNewWindow(url) || isLocalNewWindowWithRequest(url)
                else
                    isOuterNewApplication(url)
            }
            else -> true
        }
    }

    override fun newApplication(url: Uri): Boolean {
        Logger.log(Logger.LogType.INFO, "WEBVIEW newApplication $host : $url")
        return if (isLocalNewWindow(url.toString()))
            newWindow(url)
        else
            super.newApplication(url)
    }

    override fun basicNewApplication(url: String): Boolean {
        Logger.log(
            Logger.LogType.INFO,
            "WEBVIEW basicNewApplication $host : $url",
            UrlUtil.isCurrentHost(host, url).toString()
        )
        return when {
            UrlUtil.isCurrentHost(host, url) || UrlUtil.isCurrentDomain(APP_URL, url) -> {
                if (isContainsUrl(url, localBasicNewApplication)) {
                    webView.loadUrl(url)
                }
                false
            }
            isOuterNewApplication(url) -> {
                webView.loadUrl(url)
                true
            }
            else -> localNewApplication(url)
        }
    }
}