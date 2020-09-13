package com.digitaldetox.aww.upload


import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.digitaldetox.aww.persistence.UserloanrequestDao
import com.digitaldetox.aww.session.SessionManager
import com.digitaldetox.aww.util.Constants
import com.digitaldetox.aww.work.DaggerWorkerFactory
import net.gotev.uploadservice.MultipartUploadRequest
import net.gotev.uploadservice.UploadNotificationConfig
import javax.inject.Inject

class ImageUploadWorkerUserloanrequestCreate @Inject constructor(
    private val userloanrequestDao: UserloanrequestDao,
    var delegateUserloanrequestCreate: UploadStatusDelegateUserloanrequestCreate,
    private val context: Context,
    workerParams: WorkerParameters,
    sessionManager: SessionManager
) :
    Worker(context, workerParams) {

    @Inject
    lateinit var sessionManager: SessionManager

    var sessionManagerToken = sessionManager.cachedToken.value?.token

    override fun doWork(): Result {

        // Get a list of pending uploads
        val pendingUploads = userloanrequestDao.findAllPendingUploads()
        if (pendingUploads.isEmpty()) {
            // Nothing to upload? Great.
            return Result.success()
        }


        pendingUploads.forEach {


            upload(
                urls = listOf(it.title),
                body = it.body,
                loanamount = it.loanamount,
                title = it.title,
                username = it.authorsender,
                token = "Token ${sessionManagerToken}",
                subredditName = it.subreddit,
                delegateUserloanrequestCreate = delegateUserloanrequestCreate
            )
        }

        return Result.retry()
    }

    private fun upload(
        urls: List<String>,
        body: String,
        loanamount: Int,
        title: String,
        token: String,
        username: String,
        subredditName: String,
        delegateUserloanrequestCreate: UploadStatusDelegateUserloanrequestCreate
    ) {

        try {
            urls.forEach {
                val request =
                    MultipartUploadRequest(
                        context,
                        it.toString(),
                        "${Constants.SIH_API_USERLOANREQUEST_UPLOAD_URL}${username}/${subredditName}/"
                    )
                        .setMethod("POST")
                        .addParameter("body", body)
                        .addParameter("title", title)
                        .addParameter("loanamount", loanamount.toString())
                        .setNotificationConfig(UploadNotificationConfig())
                        .setDelegate(delegateUserloanrequestCreate)
                        .setMaxRetries(2)
                request.startUpload()

            }
        } catch (exc: Exception) {
            Log.d("UploadService", exc.message, exc)
        }
    }

    companion object {
        const val TAG = "upload-userloanrequest-create"
    }

    class Factory @Inject constructor(

        private val userloanrequestDao: UserloanrequestDao,
        var delegateUserloanrequestCreate: UploadStatusDelegateUserloanrequestCreate,
        var sessionManager: SessionManager
    ) : DaggerWorkerFactory.ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker =
            ImageUploadWorkerUserloanrequestCreate(
                userloanrequestDao,
                delegateUserloanrequestCreate,
                appContext,
                params,
                sessionManager
            )


    }
}