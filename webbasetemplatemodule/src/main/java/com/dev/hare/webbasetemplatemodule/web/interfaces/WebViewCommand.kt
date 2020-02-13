package com.dev.hare.webbasetemplatemodule.web.interfaces

import android.net.Uri

interface WebViewCommand {

    fun isNewWindow(url: Uri): Boolean

    fun isNewApplication(url: Uri): Boolean

    fun load(url: Uri): Boolean

    fun newWindow(url: Uri):Boolean

    fun newWindowWithRequest(url: Uri):Boolean

    fun newApplication(url: Uri):Boolean

}