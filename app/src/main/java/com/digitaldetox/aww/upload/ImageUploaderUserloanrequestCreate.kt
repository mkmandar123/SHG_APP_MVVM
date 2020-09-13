package com.digitaldetox.aww.upload



import android.util.Log
import androidx.work.*
import java.util.concurrent.TimeUnit

private val TAG = "lgx_ImageUploaderPs"

object ImageUploaderUserloanrequestCreate {

    /**
     * Enqueues a Worker to sync pending uploads in local database with Imgur.
     *
     * It will run as soon as it finds an unmetered internet connection, and even if the app is completely killed.
     */
    fun enqueue() {
        Log.d(TAG, "enqueue: enqueue in ImageUploaderUserloanrequestCreate class is called.")
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val request: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<ImageUploadWorkerUserloanrequestCreate>()
                .setConstraints(constraints)
                .addTag(ImageUploadWorkerUserloanrequestCreate.TAG)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .build()

        WorkManager.getInstance()
            .enqueueUniqueWork("upload-create-work", ExistingWorkPolicy.APPEND, request)


//        WorkManager.getInstance()
//            .beginUniqueWork("upload-create-work", ExistingWorkPolicy.REPLACE, request)
//            .enqueue()
    }
}