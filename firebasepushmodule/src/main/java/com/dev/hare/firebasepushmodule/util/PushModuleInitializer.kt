package com.dev.hare.firebasepushmodule.util

import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import java.util.concurrent.Executors
import com.dev.hare.apputilitymodule.data.abstracts.Initializable as Initializable1

object PushModuleInitializer : Initializable1 {
    override fun initialize(context: Context) {
        WorkManager.initialize(
            context,
            Configuration.Builder()
                .setExecutor(Executors.newFixedThreadPool(8))
                .build()
        )
    }
}