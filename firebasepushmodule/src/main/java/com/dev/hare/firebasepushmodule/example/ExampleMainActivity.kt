package com.dev.hare.firebasepushmodule.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dev.hare.firebasepushmodule.R
import com.dev.hare.firebasepushmodule.basic.BasicTokenCallService
import com.dev.hare.firebasepushmodule.util.FirebaseUtil
import com.dev.hare.apputilitymodule.util.Logger


class ExampleMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseUtil.getToken(object : FirebaseUtil.OnGetTokenSuccessListener {
            override fun onSuccess(token: String) {
                BasicTokenCallService.insertToken(this@ExampleMainActivity, token, "") {
                    Logger.log(Logger.LogType.INFO, "insertToken : ${it.toString()}")
                }
            }
        })
    }

}
