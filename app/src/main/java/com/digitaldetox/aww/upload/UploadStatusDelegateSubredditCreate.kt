package com.digitaldetox.aww.upload

import android.content.Context
import android.util.Log
import com.digitaldetox.aww.models.SubredditRoom
import com.digitaldetox.aww.repository.main.CreateSubredditRepository
import com.digitaldetox.aww.util.fromJson
import com.google.gson.Gson
import kotlinx.coroutines.FlowPreview

import net.gotev.uploadservice.ServerResponse
import net.gotev.uploadservice.UploadInfo
import javax.inject.Inject
import javax.inject.Singleton


private val TAG = "lgx_UploadStatusDelega"

@FlowPreview
@Singleton
class UploadStatusDelegateSubredditCreate @Inject constructor() :
    net.gotev.uploadservice.UploadStatusDelegate {

    @Inject
    lateinit var albumDetailsRepository: CreateSubredditRepository


    override fun onProgress(context: Context?, uploadInfo: UploadInfo?) {

        albumDetailsRepository.updateUploadProgress(
            imageKey = uploadInfo?.uploadId,
            uploadedBytes = uploadInfo?.uploadedBytes?.toInt() ?: 0,
            totalBytes = uploadInfo?.totalBytes?.toInt() ?: 1
        )
    }

    override fun onError(
        context: Context?,
        uploadInfo: UploadInfo?,
        serverResponse: ServerResponse?,
        exception: Exception?
    ) {

        albumDetailsRepository.updateUploadError(
            imageKey = uploadInfo?.uploadId,
            errorMessage = exception?.toString() ?: serverResponse?.httpCode?.toString()
            ?: "Unknown error"
        )
    }


    override fun onCompleted(
        context: Context?,
        uploadInfo: UploadInfo?,
        response: ServerResponse?
    ) {
        if (response?.bodyAsString == null) {
            Log.d(TAG, "onCompleted: 55: SUBREDDIT is NULL?")
            onError(
                context,
                uploadInfo,
                response,
                Exception("Upload was successful but server response_server description was null")
            )
            return
        }
        try {

            val image = Gson().fromJson<SubredditRoom>(response.bodyAsString)

            if (image != null) {

                albumDetailsRepository.updateUploadSuccess(

                    imageKey = uploadInfo?.uploadId,
                    responseImage = image
                )
            } else {
                Log.d(TAG, "onCompleted: 92: SUBREDDIT else --> image?.results is NULL ")
            }
        } catch (e: Exception) {
            Log.d(TAG, "onCompleted: 91: SUBREDDIT catch  ${e.toString()}")
            onError(context, uploadInfo, response, e)
        }
    }

    override fun onCancelled(context: Context?, uploadInfo: UploadInfo?) {
        albumDetailsRepository.updateUploadCanceled(uploadInfo?.uploadId)
    }
}