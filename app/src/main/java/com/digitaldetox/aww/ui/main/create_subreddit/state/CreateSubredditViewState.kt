package com.digitaldetox.aww.ui.main.create_subreddit.state

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

const val CREATE_SUBREDDIT_VIEW_STATE_BUNDLE_KEY = "com.digitaldetox.openapi.ui.main.create_subreddit.state.CreateSubredditViewState"

@Parcelize
data class CreateSubredditViewState(


    var subredditFields: NewSubredditFields = NewSubredditFields()

) : Parcelable {

    @Parcelize
    data class NewSubredditFields(
        var newSubredditTitle: String? = null,
        var newSubredditBody: String? = null,
        var newImageUri: Uri? = null
    ) : Parcelable
}