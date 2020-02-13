package com.dev.hare.firebasepushmodule.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dev.hare.firebasepushmodule.services.abstracts.images.AbstractImageDownloadService.Companion.model

class NotifyWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    @SuppressLint("RestrictedApi")
    override fun doWork(): Result {
        model?.runNotification()
        return Result.success()
    }

}