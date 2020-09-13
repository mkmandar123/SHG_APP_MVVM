package com.digitaldetox.aww.repository.main

import android.util.Log
import com.digitaldetox.aww.api.main.OpenApiMainService
import com.digitaldetox.aww.di.main.MainScope
import com.digitaldetox.aww.models.UserloanrequestRoom
import com.digitaldetox.aww.persistence.UserloanrequestDao
import com.digitaldetox.aww.session.SessionManager
import com.digitaldetox.aww.util.AppExecutors
import com.digitaldetox.aww.util.RateLimiter
import com.digitaldetox.aww.util.StateEvent
import kotlinx.coroutines.FlowPreview
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@FlowPreview
@MainScope
class CreateUserloanrequestRepositoryImpl
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val userloanrequestDao: UserloanrequestDao,
    val sessionManager: SessionManager,
    val executors: AppExecutors
) : CreateUserloanrequestRepository {

    private val TAG: String = "AppDebug"

    private val rateLimiter = RateLimiter<String>(30, TimeUnit.SECONDS)

    private val rateLimitKey = "albums"

    override fun createNewUserloanrequest(
        albumId: String,
        title: String,
        body: String,
        loanamount: Int,
        subreddit: String,
        authorsender: String,
        stateEvent: StateEvent,
        onAdded: (() -> Unit)?
    ) {
        val newUserloanrequestUp =
            UserloanrequestRoom(
                pk = (322323..90000213).random(),

                title = title,
                body = body,
                loanamount = loanamount,
                authorsender = authorsender,
                subreddit = subreddit,
                albumId = albumId,
                queuedForUpload = true
            )
        executors.diskIO().execute {
            Log.d(TAG, "addPendingUploads: inside executors.diskIO().execute")
            userloanrequestDao.insertUp(newUserloanrequestUp)
            if (onAdded != null) {
                Log.d(TAG, "addPendingUploads: onAdded != null")
                onAdded()
            }
        }
        rateLimiter.reset(rateLimitKey)
    }



    override fun updateUploadProgress(imageKey: String?, uploadedBytes: Int, totalBytes: Int) {

        executors.diskIO().execute {
            Log.d(TAG, "updateUploadProgress: this is called.")
            Log.d(
                TAG,
                "updateUploadProgress: 100:imageKey --> returns file:"
            )
            Log.d(
                TAG,
                "updateUploadProgress: 100:uploadedBytes --> returns 103 --> 54282 ${uploadedBytes}"
            )
            Log.d(
                TAG,
                "updateUploadProgress: 100:totalBytes --> returns 54282 --> 54282 ${totalBytes}"
            )

            val temporaryImage: UserloanrequestRoom = getImageInUploadFromDb(imageKey) ?: return@execute
            temporaryImage.uploadedPercentage = uploadedBytes / totalBytes * 100
            userloanrequestDao.updateUp(temporaryImage)
        }
    }

    override fun updateUploadError(imageKey: String?, errorMessage: String) {

        executors.diskIO().execute {
            Log.d(TAG, "updateUploadError: this is called.")
            val temporaryImage: UserloanrequestRoom = getImageInUploadFromDb(imageKey) ?: return@execute
            temporaryImage.uploadError = errorMessage
            userloanrequestDao.updateUp(temporaryImage)
        }


    }

    override fun updateUploadSuccess(imageKey: String?, responseImage: UserloanrequestRoom) {

        executors.diskIO().execute {

            val temporaryImage: UserloanrequestRoom = getImageInUploadFromDb(imageKey) ?: return@execute

            responseImage.albumId = temporaryImage.albumId
            userloanrequestDao.deleteUp(temporaryImage)
            userloanrequestDao.insertUp(responseImage)
        }

    }

    override fun updateUploadCanceled(imageKey: String?) {

        executors.diskIO().execute {
            Log.d(TAG, "updateUploadCanceled: this is called.")
            val temporaryImage: UserloanrequestRoom = getImageInUploadFromDb(imageKey) ?: return@execute
            userloanrequestDao.deleteUp(temporaryImage)
        }

    }

    override fun getImageInUploadFromDb(key: String?): UserloanrequestRoom {

        return userloanrequestDao.findFirstByKeyOnceUp(key ?: "")!!

    }


}