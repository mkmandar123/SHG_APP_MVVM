package com.digitaldetox.aww.upload


import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.digitaldetox.aww.persistence.UsersavingrequestDao
import com.digitaldetox.aww.session.SessionManager
import com.digitaldetox.aww.util.Constants
import com.digitaldetox.aww.work.DaggerWorkerFactory
import net.gotev.uploadservice.MultipartUploadRequest
import net.gotev.uploadservice.UploadNotificationConfig
import javax.inject.Inject

class ImageUploadWorkerUsersavingrequestCreate @Inject constructor(
    private val usersavingrequestDao: UsersavingrequestDao,
    var delegateUsersavingrequestCreate: UploadStatusDelegateUsersavingrequestCreate,
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
        val pendingUploads = usersavingrequestDao.findAllPendingUploads()
        if (pendingUploads.isEmpty()) {
            // Nothing to upload? Great.
            return Result.success()
        }


        pendingUploads.forEach {


            upload(
                urls = listOf(it.title),
                body = it.body,
                title = it.title,
                savingamount = it.savingamount,
                username = it.authorsender,
                token = "Token ${sessionManagerToken}",
                subredditName = it.subreddit,
                delegateUsersavingrequestCreate = delegateUsersavingrequestCreate
            )
        }

        return Result.retry()
    }

    private fun upload(
        urls: List<String>,
        body: String,
        title: String,
        savingamount: Int,
        token: String,
        username: String,
        subredditName: String,
        delegateUsersavingrequestCreate: UploadStatusDelegateUsersavingrequestCreate
    ) {

        try {
            urls.forEach {
                val request =
                    MultipartUploadRequest(
                        context,
                        it.toString(),
                        "${Constants.SIH_API_USERSAVINGREQUEST_UPLOAD_URL}${username}/${subredditName}/"
                    )
                        .setMethod("POST")
                        .addParameter("body", body)
                        .addParameter("title", title)
                        .addParameter("savingamount", savingamount.toString())
                        .setNotificationConfig(UploadNotificationConfig())
                        .setDelegate(delegateUsersavingrequestCreate)
                        .setMaxRetries(2)
                request.startUpload()

            }
        } catch (exc: Exception) {
            Log.d("UploadService", exc.message, exc)
        }
    }

    companion object {
        const val TAG = "upload-usersavingrequest-create"
    }

    class Factory @Inject constructor(

        private val usersavingrequestDao: UsersavingrequestDao,
        var delegateUsersavingrequestCreate: UploadStatusDelegateUsersavingrequestCreate,
        var sessionManager: SessionManager
    ) : DaggerWorkerFactory.ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker =
            ImageUploadWorkerUsersavingrequestCreate(
                usersavingrequestDao,
                delegateUsersavingrequestCreate,
                appContext,
                params,
                sessionManager
            )


    }
}