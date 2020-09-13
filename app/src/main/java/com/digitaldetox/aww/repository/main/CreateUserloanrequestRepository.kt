package com.digitaldetox.aww.repository.main

import com.digitaldetox.aww.di.main.MainScope
import com.digitaldetox.aww.models.UserloanrequestRoom
import com.digitaldetox.aww.util.StateEvent
import kotlinx.coroutines.FlowPreview

@FlowPreview
@MainScope
interface CreateUserloanrequestRepository {

    fun createNewUserloanrequest(
        albumId: String,
        title: String,
        body: String,
        loanamount: Int,
        subreddit: String,
        authorsender: String,
        stateEvent: StateEvent,
        onAdded: (() -> Unit)?
    )



    fun updateUploadProgress(imageKey: String?, uploadedBytes: Int, totalBytes: Int)

    fun updateUploadError(imageKey: String?, errorMessage: String)


    fun updateUploadSuccess(imageKey: String?, responseImage: UserloanrequestRoom)

    fun updateUploadCanceled(imageKey: String?)

    fun getImageInUploadFromDb(key: String?): UserloanrequestRoom
}