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

abstract class BaseIntroActivity : BaseActivity() {
    protected abstract val launchTimeOut: Long
    protected abstract val splashTimeOut: Long
    protected abstract val introImageID: Int
    protected abstract val imageView: ImageView
    protected var imageUrl: String? = null


    private val options = RequestOptions()
        .centerCrop()
        // .placeholder(introImageID)
        .error(introImageID)
        .priority(Priority.HIGH)
        .diskCacheStrategy(DiskCacheStrategy.NONE)

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        onCreateInit(savedInstanceState)
        Handler().postDelayed({ init() }, launchTimeOut)
    }

    protected fun applySplash() {
        Glide.with(this)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .timeout(5000)
            .apply(options)
            .apply(RequestOptions.skipMemoryCacheOf(true))
            .thumbnail(0.5f)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    finish()
                    overridePendingTransition(0, 0)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    Handler().postDelayed({
                        finish()
                        overridePendingTransition(0, 0)
                    }, splashTimeOut)
                    return false
                }
            })
            .into(imageView)
    }

    override fun onCreateAfter(savedInstanceState: Bundle?) {}

    abstract fun init()
}
