package com.digitaldetox.aww.upload

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.digitaldetox.aww.persistence.SubredditDao
import com.digitaldetox.aww.session.SessionManager
import com.digitaldetox.aww.util.Constants

import com.digitaldetox.aww.work.DaggerWorkerFactory
import net.gotev.uploadservice.MultipartUploadRequest
import net.gotev.uploadservice.UploadNotificationConfig
import javax.inject.Inject

class ImageUploadWorkerSubredditCreate @Inject constructor(
    private val subredditDao: SubredditDao,
    var delegateSubredditCreate: UploadStatusDelegateSubredditCreate,
    private val context: Context,
    workerParams: WorkerParameters,
    sessionManager: SessionManager
) :
    Worker(context, workerParams) {

    @Inject
    lateinit var sessionManager: SessionManager

    var sessionManagerToken = sessionManager.cachedToken.value?.token

    override fun doWork(): Result {

        val pendingUploads = subredditDao.findAllPendingUploads()
        if (pendingUploads.isEmpty()) {

            return Result.success()
        }

        pendingUploads.forEach {

            Log.d(
                TAG,
                "doWork: 43: it.title --> ${it.title} it.albumId -->  ${it.albumId} it.id --> ${it.pk}"
            )

            upload(
                urls = listOf(it.title),
                body = it.description,
                token = "Token ${sessionManagerToken}",
                delegateSubredditCreate = delegateSubredditCreate
            )


        }


        return Result.retry()
    }

    private fun upload(
        urls: List<String>,
        body: String,
        token: String,
        delegateSubredditCreate: UploadStatusDelegateSubredditCreate
    ) {

        try {
            urls.forEach {
                val request =
                    MultipartUploadRequest(
                        context,
                        it.toString(),
                        Constants.SIH_API_SUBREDDIT_UPLOAD_URL
                    )
                        .addHeader("Authorization", token)
                        .setMethod("POST")
                        .addParameter("description", body)
                        .addParameter("title", it)
                        .setNotificationConfig(UploadNotificationConfig())
                        .setDelegate(delegateSubredditCreate)
                        .setMaxRetries(2)
                request.startUpload()


            }
        } catch (exc: Exception) {
            Log.d("UploadService", exc.message, exc)
        }
    }

    companion object {
        const val TAG = "upload-subreddit-create"
    }

    class Factory @Inject constructor(

        private val subredditDao: SubredditDao,
        var delegateSubredditCreate: UploadStatusDelegateSubredditCreate,
        var sessionManager: SessionManager
    ) : DaggerWorkerFactory.ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker =
            ImageUploadWorkerSubredditCreate(
                subredditDao,
                delegateSubredditCreate,
                appContext,
                params,
                sessionManager
            )
    }
}