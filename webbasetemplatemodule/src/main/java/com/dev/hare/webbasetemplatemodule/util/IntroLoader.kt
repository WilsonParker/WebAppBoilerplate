package com.dev.hare.webbasetemplatemodule.util

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Handler
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

class IntroLoader(
    private val activity: Activity,
    private val introLoadListener: IntroLoadListener = object : IntroLoadListener {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any,
            target: Target<Drawable>,
            isFirstResource: Boolean
        ): Boolean {
            activity.finish()
            activity.overridePendingTransition(0, 0)
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
                activity.finish()
                activity.overridePendingTransition(0, 0)
            }, 2000)
            return false
        }

    }
) {
    fun applySplash(introImageID: Int, imageView: ImageView, imageUrl: String) {
        val options = RequestOptions()
            .centerCrop()
            // .placeholder(introImageID)
            .error(introImageID)
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.NONE)

        Glide.with(activity)
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
                    return introLoadListener.onLoadFailed(e, model, target, isFirstResource)
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    return introLoadListener.onResourceReady(resource, model, target, dataSource, isFirstResource)
                }
            })
            .into(imageView)
    }

    interface IntroLoadListener {
        fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean
        fun onResourceReady(
            resource: Drawable,
            model: Any,
            target: Target<Drawable>,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean
    }
}