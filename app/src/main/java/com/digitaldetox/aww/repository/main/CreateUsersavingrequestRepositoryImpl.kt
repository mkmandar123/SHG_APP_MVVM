package com.digitaldetox.aww.repository.main

import android.util.Log
import com.digitaldetox.aww.api.main.OpenApiMainService
import com.digitaldetox.aww.di.main.MainScope
import com.digitaldetox.aww.models.UsersavingrequestRoom
import com.digitaldetox.aww.persistence.UsersavingrequestDao
import com.digitaldetox.aww.session.SessionManager
import com.digitaldetox.aww.util.AppExecutors
import com.digitaldetox.aww.util.RateLimiter
import com.digitaldetox.aww.util.StateEvent
import kotlinx.coroutines.FlowPreview
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@FlowPreview
@MainScope
class CreateUsersavingrequestRepositoryImpl
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val usersavingrequestDao: UsersavingrequestDao,
    val sessionManager: SessionManager,
    val executors: AppExecutors
) : CreateUsersavingrequestRepository {

    private val TAG: String = "AppDebug"

    private val rateLimiter = RateLimiter<String>(30, TimeUnit.SECONDS)

    private val rateLimitKey = "albums"

    override fun createNewUsersavingrequest(
        albumId: String,
        title: String,
        body: String,
        savingamount: Int,
        subreddit: String,
        authorsender: String,
        stateEvent: StateEvent,
        onAdded: (() -> Unit)?
    ) {
        val newUsersavingrequestUp =
            UsersavingrequestRoom(
                pk = (322323..90000213).random(),

                title = title,
                body = body,
                savingamount = savingamount,
                authorsender = authorsender,
                subreddit = subreddit,
                albumId = albumId,
                queuedForUpload = true
            )
        executors.diskIO().execute {
            Log.d(TAG, "addPendingUploads: inside executors.diskIO().execute")
            usersavingrequestDao.insertUp(newUsersavingrequestUp)
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

            val temporaryImage: UsersavingrequestRoom = getImageInUploadFromDb(imageKey) ?: return@execute
            temporaryImage.uploadedPercentage = uploadedBytes / totalBytes * 100
            usersavingrequestDao.updateUp(temporaryImage)
        }
    }

    override fun updateUploadError(imageKey: String?, errorMessage: String) {

        executors.diskIO().execute {
            Log.d(TAG, "updateUploadError: this is called.")
            val temporaryImage: UsersavingrequestRoom = getImageInUploadFromDb(imageKey) ?: return@execute
            temporaryImage.uploadError = errorMessage
            usersavingrequestDao.updateUp(temporaryImage)
        }


    }

    override fun updateUploadSuccess(imageKey: String?, responseImage: UsersavingrequestRoom) {

        executors.diskIO().execute {

            val temporaryImage: UsersavingrequestRoom = getImageInUploadFromDb(imageKey) ?: return@execute

            responseImage.albumId = temporaryImage.albumId
            usersavingrequestDao.deleteUp(temporaryImage)
            usersavingrequestDao.insertUp(responseImage)
        }

    }

    override fun updateUploadCanceled(imageKey: String?) {

        executors.diskIO().execute {
            Log.d(TAG, "updateUploadCanceled: this is called.")
            val temporaryImage: UsersavingrequestRoom = getImageInUploadFromDb(imageKey) ?: return@execute
            usersavingrequestDao.deleteUp(temporaryImage)
        }

    }

    override fun getImageInUploadFromDb(key: String?): UsersavingrequestRoom {

        return usersavingrequestDao.findFirstByKeyOnceUp(key ?: "")!!

    }


}