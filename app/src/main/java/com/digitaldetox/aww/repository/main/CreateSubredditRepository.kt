package com.digitaldetox.aww.repository.main

import com.digitaldetox.aww.di.main.MainScope
import com.digitaldetox.aww.models.SubredditRoom
import com.digitaldetox.aww.util.StateEvent
import kotlinx.coroutines.FlowPreview

@FlowPreview
@MainScope
interface CreateSubredditRepository {

    fun createNewSubreddit(
        albumId: String,
        title: String,
        body: String,
        stateEvent: StateEvent,
        onAdded: (() -> Unit)?
    )



    fun updateUploadProgress(imageKey: String?, uploadedBytes: Int, totalBytes: Int)

    fun updateUploadError(imageKey: String?, errorMessage: String)


    fun updateUploadSuccess(imageKey: String?, responseImage: SubredditRoom)

    fun updateUploadCanceled(imageKey: String?)

    fun getImageInUploadFromDb(key: String?): SubredditRoom
}