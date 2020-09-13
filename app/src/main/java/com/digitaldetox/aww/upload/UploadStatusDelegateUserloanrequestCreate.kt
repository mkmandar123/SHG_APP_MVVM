package com.digitaldetox.aww.upload



import android.content.Context
import android.util.Log
import com.digitaldetox.aww.models.UserloanrequestRoom
import com.digitaldetox.aww.repository.main.CreateUserloanrequestRepository
import com.digitaldetox.aww.util.fromJson
import com.google.gson.Gson

import net.gotev.uploadservice.ServerResponse
import net.gotev.uploadservice.UploadInfo
import javax.inject.Inject
import javax.inject.Singleton

/* From Author: UploadStatusDelegateSubredditCreate will update this temporary image record in db to update UI for various states like pending, uploading, error, and success */

private val TAG = "lgx_UploadStatusDelega"

@Singleton
class UploadStatusDelegateUserloanrequestCreate @Inject constructor() : net.gotev.uploadservice.UploadStatusDelegate {

    @Inject
    lateinit var albumDetailsRepository: CreateUserloanrequestRepository

    /* Called when the upload progress changes. */

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
        Log.d(
            TAG,
            "onError: 42: USERPOST onError uploadInfo?.uploadId --> ${uploadInfo?.uploadId}"
        )
        Log.d(TAG, "onError: 42: USERPOST onError serverResponse --> ${serverResponse.toString()}")
        Log.d(TAG, "onError: 42: USERPOST onError exception --> ${exception.toString()}")
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
            Log.d(TAG, "onCompleted: 55: USERPOST is NULL?")
            onError(
                context,
                uploadInfo,
                response,
                Exception("Upload was successful but server response_server description was null")
            )
            Log.d(TAG, "onCompleted: 62: USERPOST going to return ")
            return
        }
        try {
            Log.d(TAG, "onCompleted: 64: USERPOST inside try") // WE ARE HERE
            /* bodyAsString --> /**
     * Gets server response_server description as string.
     * If the string is a JSON, you can parse it using a library such as org.json
     * (embedded in Android) or google's gson
     * @return string
     */ */

            /* From Author: When upload succeeds, delegateSubredditCreate will parse response_server from Imgur and pass it on to updateUploadSuccess of AlbumDetailsRepository: */

//            val image = Gson().fromJson<ImgurResponse<SubredditRoom>>(response.bodyAsString)
            val image = Gson().fromJson<UserloanrequestRoom>(response.bodyAsString)

            if (image != null) {

                Log.d(TAG, "onCompleted: 74: USERPOST image --> ${image}")


                Log.d(
                    TAG,
                    "onCompleted: 55: returns ImgurResponse(results=AlbumImage(id=Wgglj3A, title=46281, description=null, datetime=1583927231, animated=false, width=634, height=573, size=140740, views=0, link=https://i.imgur.com/Wgglj3A.jpg, albumId=null, queuedForUpload=false, uploadedPercentage=null, uploadId=null, uploadError=null), success=true, status=200, errorMessage=null) ${image}"
                )


//                Log.d(
//                    TAG,
//                    "onCompleted: 58: returns AlbumImage(id=Wgglj3A, title=46281, description=null, datetime=1583927231, animated=false, width=634, height=573, size=140740, views=0, link=https://i.imgur.com/Wgglj3A.jpg, albumId=null, queuedForUpload=false, uploadedPercentage=null, uploadId=null, uploadError=null) ${image?.results}"
//                )


                albumDetailsRepository.updateUploadSuccess(

                    imageKey = uploadInfo?.uploadId,
                    responseImage = image
                )
            } else {
                Log.d(TAG, "onCompleted: 92: USERPOST else --> image?.results is NULL ")
            }
        } catch (e: Exception) {
            Log.d(TAG, "onCompleted: 91: USERPOST catch  ${e.toString()}")
            onError(context, uploadInfo, response, e)
        }
    }

    override fun onCancelled(context: Context?, uploadInfo: UploadInfo?) {
        albumDetailsRepository.updateUploadCanceled(uploadInfo?.uploadId)
    }
}