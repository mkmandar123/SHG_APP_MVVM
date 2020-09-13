package com.digitaldetox.aww.upload

import android.annotation.SuppressLint
import android.util.Log
import androidx.work.*
import java.util.concurrent.TimeUnit

private val TAG = "lgx_ImageUploaderSubredditCreate"

object ImageUploaderSubredditCreate {

    
    @SuppressLint("LongLogTag")
    fun enqueue() {
        Log.d(TAG, "enqueue: enqueue in ImageUploaderSubredditCreate class is called.")
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val request: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<ImageUploadWorkerSubredditCreate>()
                .setConstraints(constraints)
                .addTag(ImageUploadWorkerSubredditCreate.TAG)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .build()

        WorkManager.getInstance()
            .beginUniqueWork("upload-create-work", ExistingWorkPolicy.REPLACE, request)
            .enqueue()
    }
}