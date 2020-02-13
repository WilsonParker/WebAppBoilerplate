package com.dev.hare.apputilitymodule.util.view

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.net.http.SslError
import android.os.Build
import android.webkit.SslErrorHandler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

object AlertUtil {

    fun alert(activity: Activity, message: String) {
        val alert = AlertDialog.Builder(activity).apply {
            setMessage(message)
            setCancelable(false)
            setPositiveButton(
                "확인"
            ) { dialog, which ->
                dialog.cancel()
                activity.finish()
            }
        }.create()
        alert.show()
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#55B5A4"))
    }

    fun sslAlertShow(context: Context, handler: SslErrorHandler, error: SslError) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            handler.proceed()
            return
        }
        val builder = android.app.AlertDialog.Builder(context)
        builder.setMessage("ssl 인증서가 올바르지 않습니다. 계속 진행하시겠습니까?")
        builder.setPositiveButton("확인") { dialog, which -> handler.proceed() }
        builder.setNegativeButton(
            "취소"
        ) { dialog, which -> handler.cancel() }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            builder.setOnDismissListener {
                handler.cancel()
                (context as AppCompatActivity).finish()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
}