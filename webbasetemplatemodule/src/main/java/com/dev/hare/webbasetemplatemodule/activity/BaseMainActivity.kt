package com.dev.hare.webbasetemplatemodule.activity

import android.content.Intent
import android.os.Bundle
import com.dev.hare.apputilitymodule.util.view.AlertUtil
import com.dev.hare.webbasetemplatemodule.util.CommonUtil
import com.dev.hare.webbasetemplatemodule.util.UrlUtil
import kotlin.reflect.KClass

abstract class BaseMainActivity<activity : BaseIntroActivity> : BaseWebActivity() {
    abstract val introClass: KClass<activity>

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateInit(savedInstanceState)
        if (CommonUtil.isNetworkState(applicationContext) === CommonUtil.NetworkState.NOT_CONNECTED) {
            AlertUtil.alert(this, "네트워크 신호가 원활 하지 않습니다\n확인 후 다시 접속 바랍니다")
        } else {
            val introIntent = Intent(this, introClass.java)
            introIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            startActivity(introIntent)
        }
        onCreateAfter(savedInstanceState)
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