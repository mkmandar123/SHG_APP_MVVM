package com.digitaldetox.aww.ui.main.create_userloanrequest.state


import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

const val CREATE_USERLOANREQUEST_VIEW_STATE_BUNDLE_KEY =
    "com.digitaldetox.openapi.ui.main.create_userloanrequest.state.CreateUserloanrequestViewState"

@Parcelize
data class CreateUserloanrequestViewState(


    var userloanrequestFields: NewUserloanrequestFields = NewUserloanrequestFields()

) : Parcelable {

    @Parcelize
    data class NewUserloanrequestFields(
        var newUserloanrequestTitle: String? = null,
        var newUserloanrequestBody: String? = null,
        var newUserloanrequestLoanamount: Int? = null,
        var newImageUri: Uri? = null
    ) : Parcelable
}