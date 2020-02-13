package com.dev.hare.webbasetemplatemodule.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import com.dev.hare.apputilitymodule.util.view.AlertUtil
import com.dev.hare.webbasetemplatemodule.util.CommonUtil
import com.dev.hare.webbasetemplatemodule.util.IntroLoader
import com.dev.hare.webbasetemplatemodule.util.UrlUtil

abstract class BaseMainWithIntroImageActivity : BaseWebActivity() {
    abstract val introImageID: Int
    abstract val introImageView: ImageView
    abstract val mainView: ViewGroup
    protected open val splashTimeOut: Long = 2000

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateInit(savedInstanceState)
        if (CommonUtil.isNetworkState(applicationContext) === CommonUtil.NetworkState.NOT_CONNECTED) {
            AlertUtil.alert(this, "네트워크 신호가 원활 하지 않습니다\n확인 후 다시 접속 바랍니다")
        } else {
            init()
        }
        onCreateAfter(savedInstanceState)
    }

    abstract fun init()

    protected fun loadIntro(imageUrl: String) {
        val introLoader = IntroLoader(this, object : IntroLoader.IntroLoadListener {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                Handler().postDelayed({
                    introImageView.visibility = View.GONE
                    mainView.visibility = View.VISIBLE
                }, splashTimeOut)
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
                    introImageView.visibility = View.GONE
                    mainView.visibility = View.VISIBLE
                }, splashTimeOut)
                return false
            }

        })
        introImageView.visibility = View.VISIBLE
        introLoader.applySplash(introImageID, introImageView, imageUrl)
    }

    /**
     * Notification 을 통해 App 을 실행했을 경우
     * link 를 그렇지 않다면 defHost 를 return 합니다
     *
     * @param       defHost: String
     * @return      String
     * @author      Hare
     * @added       2019-06-21
     * @updated     2019-06-21
     * */
    protected fun getLink(defHost: String): String {
        return if (intent.hasExtra("link")) {
            /**
             * link 가 https?://example.com 주소가 제거되어 입력이 되므로
             * host 뒤에 link 를 붙였을 경우 / 로 구분이 안될 때를 대비하여 defHost 에 /를 붙입니다
             * @author      Hare
             * @added       2019-06-21
             * @updated     2019-06-21
             * */
            UrlUtil.appendLastSlashUrl(defHost) + intent.getStringExtra("link")
        } else defHost
    }
}