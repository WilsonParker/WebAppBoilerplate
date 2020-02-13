package com.dev.hare.webbasetemplatemodule.web

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import com.dev.hare.apputilitymodule.util.VersionChecker
import com.dev.hare.webbasetemplatemodule.activity.BaseWindowActivity
import com.dev.hare.webbasetemplatemodule.util.UrlUtil

abstract class BaseWebView<Activity : BaseWindowActivity> : WebView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var webViewCommand: BaseWebViewCommand<Activity>? = null
        set(value) {
            field = value
            webViewClient = BaseWebViewClient(value)
            webChromeClient = BaseWebChromeClient(context, value)
        }
    abstract var host: String

    var javascriptBrideInterface: JavascriptBridgeFrame? = null
        @SuppressLint("JavascriptInterface")
        set(value) {
            field = value
            addJavascriptInterface(value, "android")
        }

    init {
        init()
    }

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    protected open fun init() {
        settings.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                val cookieManager = CookieManager.getInstance()
                cookieManager.setAcceptCookie(true)
                cookieManager.setAcceptThirdPartyCookies(this@BaseWebView, true)
            }

            // 웹뷰 기타설정
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                safeBrowsingEnabled = true  // api 26
            }

            cacheMode = WebSettings.LOAD_DEFAULT
            setAppCacheEnabled(true)
            setAppCachePath(context.cacheDir.path)
            // setSavePassword(false)
            setGeolocationEnabled(true)
            setRenderPriority(WebSettings.RenderPriority.HIGH)

            setSupportZoom(true)
            builtInZoomControls = true
            //줌컨트롤러 안보이게 처리
            displayZoomControls = false

            // 웹뷰 이미지 허용설정
            blockNetworkImage = false
            // 웹뷰 이미지 자동로드 설정
            loadsImagesAutomatically = true
            useWideViewPort = true
            loadWithOverviewMode = true
            mediaPlaybackRequiresUserGesture = false

            domStorageEnabled = true
            javaScriptEnabled = true
            allowFileAccess = true
            allowFileAccessFromFileURLs = true // Maybe you don't need this rule
            allowUniversalAccessFromFileURLs = true
            allowContentAccess = true
            javaScriptCanOpenWindowsAutomatically = true
            /*
                활성화 했을 경우 chrome 브라우저로 실행할 페이지를 webview 에서도 호출하거나 하얀 페이지로 바뀌는 문제 발생
            */
            // setSupportMultipleWindows(true)
            // layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
            // 웹뷰 텍스트줌 설정
            textZoom = 100
            pluginState = WebSettings.PluginState.OFF
            userAgentString = "$userAgentString/app_android/app_version:${VersionChecker.getVersionInfo(context)}"
        }

        //웹뷰 속도개선
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }

//         clearHistory()
//         clearFormData()
//         clearCache(true)
//         clearView()
//         setWebContentsDebuggingEnabled(true)

//         scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        setMixedContentAllowed(true)
        setThirdPartyCookiesEnabled(true)

        webViewClient = BaseWebViewClient(webViewCommand)
        webChromeClient = BaseWebChromeClient(context, webViewCommand)
    }

    fun setMixedContentAllowed(allowed: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode =
                if (allowed) WebSettings.MIXED_CONTENT_ALWAYS_ALLOW else WebSettings.MIXED_CONTENT_NEVER_ALLOW
        }
    }

    fun setThirdPartyCookiesEnabled(enabled: Boolean) {
        CookieManager.getInstance().setAcceptCookie(enabled)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, enabled)
        }
    }

    fun historyBack(url: String, host: String, event: () -> Unit) {
        if (UrlUtil.isSameUrl(host, url)) {
            event()
        } else if (UrlUtil.isCurrentDomain(host, url) && canGoBack()) {
            goBack()
        } else {
            event()
        }
    }

    fun home() {
        loadUrl(host)
//        reload()
//        clearHistory()
    }

    interface JavascriptBridgeFrame
}