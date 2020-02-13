package com.dev.hare.webbasetemplatemodule.web

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Message
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.dev.hare.webbasetemplatemodule.activity.BaseWindowActivity
import com.dev.hare.webbasetemplatemodule.util.FileChooserManagerRenewer

open class BaseWebChromeClient<activity : BaseWindowActivity>(
    private val context: Context,
    private val webViewBaseCommand: BaseWebViewCommand<activity>?,
    private val createWindowType: WindowType = WindowType.AddNewWindow
) :
    WebChromeClient() {

    enum class WindowType {
        AddNewWindow, NewActivity, AddNewDialog
    }

    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        return when (createWindowType) {
            WindowType.AddNewWindow -> addNewWindow(view, isDialog, isUserGesture, resultMsg)
            WindowType.NewActivity -> newActivity(view, isDialog, isUserGesture, resultMsg)
            WindowType.AddNewDialog -> addNewDialog(view, isDialog, isUserGesture, resultMsg)
        }
    }

    /*override fun onCloseWindow(window: WebView?) {
        Logger.log(Logger.LogType.INFO, "onCloseWindow")
        // (window?.context as Activity).finish()
        (context as Activity).finish()
    }*/

    // For Android Version 5.0+
    // Ref: https://github.com/GoogleChrome/chromium-webview-samples/blob/master/input-file-example/app/src/main/java/inputfilesample/android/chrome/google/com/inputfilesample/MainFragment.java
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams
    ): Boolean {
        return FileChooserManagerRenewer.onShowFileChooser(webView, filePathCallback, fileChooserParams, context as Activity)
    }

    protected fun newActivity(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        webViewBaseCommand?.newWindow(Uri.parse(view?.url))
        return true
    }

    protected fun addNewWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        view?.settings?.apply {
            domStorageEnabled = true
            javaScriptEnabled = true
            allowFileAccess = true
            allowContentAccess = true
        }

        var newWebView = object : BaseWebView<activity>(context) {
            override var host: String = ""
        }.apply {
            this.webViewCommand = webViewBaseCommand
            this.webViewClient = BaseWebViewClient(webViewBaseCommand)
            this.webChromeClient= this@BaseWebChromeClient
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }

        // view?.removeAllViews()
        view?.webChromeClient = this
        view?.addView(newWebView)
        (resultMsg!!.obj as WebView.WebViewTransport).webView = newWebView
        resultMsg.sendToTarget()
        return true
    }

    protected fun addNewDialog(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        /*var newWebView = object : BaseWebView<activity>(context) {
            override var host: String = ""
        }.apply {
            this.webViewCommand = webViewBaseCommand
            this.webViewClient = BaseWebViewClient(webViewBaseCommand)
            this.webChromeClient= this@BaseWebChromeClient
        }*/
        var newWebView = WebView(view?.context).apply {
            settings.apply {
                javaScriptEnabled = true
            }
        }

        var dialog = Dialog(view?.context)
        dialog.setContentView(newWebView)
        dialog.show()
        newWebView.webViewClient = WebViewClient()
        newWebView.webChromeClient = object : WebChromeClient() {
            override fun onCloseWindow(window: WebView) {
                dialog.dismiss()
            }
        }

        (resultMsg?.obj as WebView.WebViewTransport).webView = newWebView
        resultMsg.sendToTarget()
        return true
    }

}