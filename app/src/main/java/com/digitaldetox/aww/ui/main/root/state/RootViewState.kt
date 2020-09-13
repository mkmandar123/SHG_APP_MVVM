package com.digitaldetox.aww.ui.main.root.state


import android.os.Parcelable
import com.digitaldetox.aww.models.*

import kotlinx.android.parcel.Parcelize

const val ROOT_VIEW_STATE_BUNDLE_KEY =
    "com.digitaldetox.openapi.ui.main.subreddit.state.RootViewState"

@Parcelize
data class RootViewState(

    var humansavingprofileFields: RootViewState.HumansavingprofileFields = RootViewState.HumansavingprofileFields(),

    var humanloanprofileFields: RootViewState.HumanloanprofileFields = RootViewState.HumanloanprofileFields(),


    var subredditFields: SubredditFields = SubredditFields(),


    var viewSubredditFields: ViewSubredditFields = ViewSubredditFields(),


    var updatedSubredditFields: UpdatedSubredditFields = UpdatedSubredditFields(),

    /* SUBUSER */

    var subuserFields: SubuserFields = SubuserFields(),

    /* end SUBUSER */

    /* USERLOANREQUEST */

    var userloanrequestFields: UserloanrequestFields = UserloanrequestFields(),


    var viewUserloanrequestFields: ViewUserloanrequestFields = ViewUserloanrequestFields(),


    var updatedUserloanrequestFields: UpdatedUserloanrequestFields = UpdatedUserloanrequestFields(),

    /* end USERLOANREQUEST */


/* USERSAVINGREQUEST */

    var usersavingrequestFields: RootViewState.UsersavingrequestFields =
        RootViewState.UsersavingrequestFields(),


    var viewUsersavingrequestFields: RootViewState.ViewUsersavingrequestFields =
        RootViewState.ViewUsersavingrequestFields(),


    var updatedUsersavingrequestFields: RootViewState.UpdatedUsersavingrequestFields =
        RootViewState.UpdatedUsersavingrequestFields()

/* end USERSAVINGREQUEST */


) : Parcelable {

    @Parcelize
    data class HumansavingprofileFields(
        var humansavingprofileList: List<HumansavingprofileRoom>? = null,
        var isQueryExhausted: Boolean? = null,
        var filter: String? = null,
        var order: String? = null,
        var layoutManagerState: Parcelable? = null
    ) : Parcelable

    @Parcelize
    data class HumanloanprofileFields(
        var humanloanprofileList: List<HumanloanprofileRoom>? = null,
        var isQueryExhausted: Boolean? = null,
        var filter: String? = null,
        var order: String? = null,
        var layoutManagerState: Parcelable? = null
    ) : Parcelable

    @Parcelize
    data class SubredditFields(
        var subredditList: List<SubredditRoom>? = null,
        var searchQuery: String? = null,
        var page: Int? = null,
        var isQueryExhausted: Boolean? = null,
        var filter: String? = null,
        var order: String? = null,
        var layoutManagerState: Parcelable? = null
    ) : Parcelable

    @Parcelize
    data class ViewSubredditFields(
        var subredditRoom: SubredditRoom? = null,
        var isAuthorOfSubreddit: Boolean? = null
    ) : Parcelable

    @Parcelize
    data class UpdatedSubredditFields(
        var updatedSubredditTitle: String? = null,
        var updatedSubredditBody: String? = null
    ) : Parcelable

    /* SUBUSER */

    @Parcelize
    data class SubuserFields(
        var subuserList: List<String>? = null,
        var searchQuery: String? = null,
        var authorsenderNamePass: String? = null,
        var page: Int? = null,
        var isQueryExhausted: Boolean? = null,
        var filter: String? = null,
        var order: String? = null,
        var layoutManagerState: Parcelable? = null
    ) : Parcelable

    /* end SUBUSER */

    /* USERLOANREQUEST */

    @Parcelize
    data class UserloanrequestFields(
        var userloanrequestList: List<UserloanrequestRoom>? = null,
        var searchQuery: String? = null,
        var page: Int? = null,
        var isQueryExhausted: Boolean? = null,
        var filter: String? = null,
        var order: String? = null,
        var layoutManagerState: Parcelable? = null
    ) : Parcelable

    @Parcelize
    data class ViewUserloanrequestFields(
        var userloanrequestRoom: UserloanrequestRoom? = null,
        var isAuthorOfUserloanrequest: Boolean? = null
    ) : Parcelable

    @Parcelize
    data class UpdatedUserloanrequestFields(
        var updatedUserloanrequestTitle: String? = null,
        var updatedUserloanrequestBody: String? = null,
        var updatedUserloanrequestLoanamount: Int? = null
    ) : Parcelable

    /* end USERLOANREQUEST */

/* USERSAVINGREQUEST */

    @Parcelize
    data class UsersavingrequestFields(
        var usersavingrequestList: List<UsersavingrequestRoom>? = null,
        var searchQuery: String? = null,
        var page: Int? = null,
        var isQueryExhausted: Boolean? = null,
        var filter: String? = null,
        var order: String? = null,
        var layoutManagerState: Parcelable? = null
    ) : Parcelable

    @Parcelize
    data class ViewUsersavingrequestFields(
        var usersavingrequestRoom: UsersavingrequestRoom? = null,
        var isAuthorOfUsersavingrequest: Boolean? = null
    ) : Parcelable

    @Parcelize
    data class UpdatedUsersavingrequestFields(
        var updatedUsersavingrequestTitle: String? = null,
        var updatedUsersavingrequestBody: String? = null,
        var updatedUsersavingrequestSavingamount: Int? = null
    ) : Parcelable

/* end USERSAVINGREQUEST */
}






