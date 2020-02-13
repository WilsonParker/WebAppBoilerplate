package com.dev.hare.webbasetemplatemodule.activity

import android.os.Bundle
import com.dev.hare.apputilitymodule.util.view.AlertUtil
import com.dev.hare.webbasetemplatemodule.R
import com.dev.hare.webbasetemplatemodule.util.CommonUtil
import com.dev.hare.webbasetemplatemodule.util.UrlUtil
import kotlin.reflect.KClass

/**
 *
 * Todo("
 *  intro type 에 따라 image, activity 로 구현
 *  다른 module, app project 의 layout, view 를 참조 가능한지 확인
 * ")
 * @author      Hare
 * @added       2019-06-26
 * @updated     2019-06-26
 * */
abstract class BaseMainWithIntroActivity<activity : BaseIntroActivity> : BaseWebActivity() {
    enum class IntroType(private val key: String) {
        IMAGE("image"),
        ACTIVITY("activity");

        fun getValue() = key
    }

    abstract val introClass: KClass<activity>?
    abstract val mainLayoutID: Int
    abstract val introType: IntroType

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_frame)
        onCreateInit(savedInstanceState)
        if (CommonUtil.isNetworkState(applicationContext) === CommonUtil.NetworkState.NOT_CONNECTED) {
            AlertUtil.alert(this, "네트워크 신호가 원활 하지 않습니다\n확인 후 다시 접속 바랍니다")
        } else {
            startIntro()
        }
        onCreateAfter(savedInstanceState)
    }

    private fun startIntro() {
       /* when (introType) {
            IntroType.IMAGE -> {
                val introLoader = IntroLoader(this, 2000, object : IntroLoader.IntroLoadListener {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })
            }
            IntroType.ACTIVITY -> {
                intro.visibility = GONE
                // 인트로 페이지 추가
                val introIntent = Intent(this, introClass?.java)
                introIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                startActivity(introIntent)
            }
        }*/
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