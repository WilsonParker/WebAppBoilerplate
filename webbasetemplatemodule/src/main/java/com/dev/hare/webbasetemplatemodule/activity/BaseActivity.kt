package com.dev.hare.webbasetemplatemodule.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    abstract fun onCreateInit(savedInstanceState: Bundle?)
    abstract fun onCreateAfter(savedInstanceState: Bundle?)

}