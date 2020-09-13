package com.digitaldetox.aww.ui.main.create_usersavingrequest.state


import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

const val CREATE_USERSAVINGREQUEST_VIEW_STATE_BUNDLE_KEY =
    "com.digitaldetox.openapi.ui.main.create_usersavingrequest.state.CreateUsersavingrequestViewState"

@Parcelize
data class CreateUsersavingrequestViewState(


    var usersavingrequestFields: NewUsersavingrequestFields = NewUsersavingrequestFields()

) : Parcelable {

    @Parcelize
    data class NewUsersavingrequestFields(
        var newUsersavingrequestTitle: String? = null,
        var newUsersavingrequestBody: String? = null,
        var newUsersavingrequestSavingamount: Int? = null,
        var newImageUri: Uri? = null
    ) : Parcelable
}