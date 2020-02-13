package com.dev.hare.webbasetemplatemodule.web

import android.app.AlertDialog
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.webkit.*
import androidx.annotation.RequiresApi
import com.dev.hare.webbasetemplatemodule.activity.BaseWindowActivity
import com.dev.hare.webbasetemplatemodule.util.UrlUtil
import com.dev.hare.webbasetemplatemodule.web.BaseWebViewCommand
import java.net.URL

open class BaseWebViewClient<Activity : BaseWindowActivity>(private val _webViewBaseCommand: BaseWebViewCommand<Activity>?) :
    WebViewClient() {

    var onPageFinishCallbackList: ArrayList<OnPageFinishListener> = ArrayList()

    /**
     * WebView Page load 될 때 실행할 callback 을 추가합니다
     *
     * @param     onPageFinishListener
     * @author    Hare
     * @added     2019-06-14
     * @updated   2019-06-14
     * */
    fun addOnPageFinishCallback(onPageFinishListener: OnPageFinishListener) {
        this.onPageFinishCallbackList.add(onPageFinishListener)
    }

    private fun newApplicationIfOtherHost(url: String): Boolean {
        var result = false
        _webViewBaseCommand?.let {
            result = if (UrlUtil.isCurrentHost(_webViewBaseCommand.host, url)) {
                _webViewBaseCommand.newApplication(Uri.parse(url))
                true
            } else
                false
        }
        return result
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        /**
         * 페이지 로드가 완료된 후에 저장한 callback 들을 실행합니다
         *
         * @author     Hare
         * @added      2019-06-14
         * @updated    2019-06-14
         * */
        onPageFinishCallbackList.forEach {
            it.onPageFinish()
            onPageFinishCallbackList.remove(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return shouldOverrideUrlLoading(view, request?.url.toString())
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        var result = false
        url?.let {
            _webViewBaseCommand?.let {
                result = it.load(Uri.parse(url))
            }
        }
        return if (!result)
            super.shouldOverrideUrlLoading(view, url)
        else
            result
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        // super.onReceivedSslError(view, handler, error)
        val builder = AlertDialog.Builder(view?.getContext())
        builder.setMessage("보안 인증서에 문제가 있습니다. 계속 진행 하시겠습니까?")
        builder.setPositiveButton("예") { dialog, which -> handler?.proceed() }
        builder.setNegativeButton("아니오") { dialog, which -> handler?.cancel() }
        val dialog = builder.create()
        dialog.show()
    }

    private fun handleRequest(urlString: String): WebResourceResponse {
        var url = URL(urlString)
        var connection = url.openConnection().apply {
            setRequestProperty("User-Agent", "")
            setRequestProperty("method", "get")
            doInput = true
        }
        connection.connect()
        var inputStream = connection.getInputStream()
        return WebResourceResponse("text/json", "utf-8", inputStream)
    }

    interface OnPageFinishListener {
        fun onPageFinish()
    }
}