package com.dev.hare.firebasepushmodule

import com.dev.hare.firebasepushmodule.models.abstracts.AbstractDefaultNotificationModel
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testEnum() {
        System.out.println(AbstractDefaultNotificationModel.PushType.valueOf("TXT"))
        System.out.println(AbstractDefaultNotificationModel.PushType.valueOf("TXT") == AbstractDefaultNotificationModel.PushType.TXT)
    }
}
