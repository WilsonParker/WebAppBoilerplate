package com.dev.hare.firebasepushmodule.services.abstracts.images

import android.app.Activity
import android.os.Build
import androidx.work.*
import com.dev.hare.firebasepushmodule.worker.DownloadImageWorker
import com.dev.hare.firebasepushmodule.worker.NotifyWorker
import com.dev.hare.firebasepushmodule.worker.WorkerConstants
import com.dev.hare.apputilitymodule.util.Logger

abstract class AbstractWorkerImageDownloadService<activity : Activity> : AbstractForegroundImageDownloadService<activity>() {

    /**
     * Worker 를 생성하여 이미지를 다운로드 받고 Notification 을 설정합니다
     *
     * @Author : Hare
     * @Update : 19.5.30
     */
    override fun run() {
        try {
            model!!.let {
                val builder = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(false)
                    .setRequiresCharging(false)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    builder.setRequiresDeviceIdle(false)
                }
                var constraints = builder.build()

                val downImageData = Data.Builder()
                downImageData.putString(WorkerConstants.KEY_IMAGE_URL, url)

                val downImage = OneTimeWorkRequest.Builder(DownloadImageWorker::class.java)
                    .setInputData(downImageData.build())
                    .setConstraints(constraints)
                    .build()

                val notify = OneTimeWorkRequest.Builder(NotifyWorker::class.java)
                    .setInputData(downImageData.build())
                    .setConstraints(constraints)
                    .build()

                 WorkManager.getInstance().beginWith(downImage).then(notify).enqueue()
            }
        } catch (e: Exception) {
            Logger.log(Logger.LogType.ERROR, "", e)
        }
    }

}
