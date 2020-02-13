package com.dev.hare.webbasetemplatemodule.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

abstract class BaseMainIntroActivity : BaseActivity() {
    protected abstract val launchTimeOut: Long
    protected abstract val introImageID: Int

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        onCreateInit(savedInstanceState)
        Handler().postDelayed({ init() }, launchTimeOut)
    }

    override fun onCreateAfter(savedInstanceState: Bundle?) {}

    abstract fun init()
}
