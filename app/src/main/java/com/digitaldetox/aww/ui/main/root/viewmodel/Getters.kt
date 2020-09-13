package com.digitaldetox.aww.ui.main.root.viewmodel


import com.digitaldetox.aww.models.*
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.SUBREDDIT_FILTER_DATE_UPDATED
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.SUBREDDIT_ORDER_DESC
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.SUBUSER_FILTER_DATE_UPDATED
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.SUBUSER_ORDER_DESC
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.USERLOANREQUEST_FILTER_DATE_UPDATED
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.USERLOANREQUEST_ORDER_DESC
import com.digitaldetox.aww.persistence.RootQueryUtils
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.HUMANLOANPROFILE_FILTER_DATE_UPDATED
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.HUMANLOANPROFILE_ORDER_DESC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getIsQueryExhaustedHumansavingprofile(): Boolean {
    return getCurrentViewStateOrNew().humansavingprofileFields.isQueryExhausted
        ?: false
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getFilterHumansavingprofile(): String {
    return getCurrentViewStateOrNew().humansavingprofileFields.filter
        ?: RootQueryUtils.HUMANSAVINGPROFILE_FILTER_DATE_UPDATED
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getOrderHumansavingprofile(): String {
    return getCurrentViewStateOrNew().humansavingprofileFields.order
        ?: RootQueryUtils.HUMANSAVINGPROFILE_ORDER_DESC
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getDummyHumansavingprofile(): HumansavingprofileRoom {
    return HumansavingprofileRoom( pk = -1, title = "", albumId = "" )
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getIsQueryExhaustedHumanloanprofile(): Boolean {
    return getCurrentViewStateOrNew().humanloanprofileFields.isQueryExhausted
        ?: false
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getFilterHumanloanprofile(): String {
    return getCurrentViewStateOrNew().humanloanprofileFields.filter
        ?: HUMANLOANPROFILE_FILTER_DATE_UPDATED
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getOrderHumanloanprofile(): String {
    return getCurrentViewStateOrNew().humanloanprofileFields.order
        ?: HUMANLOANPROFILE_ORDER_DESC
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getDummyHumanloanprofile(): HumanloanprofileRoom {
    return HumanloanprofileRoom( pk = -1, title = "", albumId = "" )
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getIsQueryExhaustedSubreddit(): Boolean {
    return getCurrentViewStateOrNew().subredditFields.isQueryExhausted
        ?: false
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getFilterSubreddit(): String {
    return getCurrentViewStateOrNew().subredditFields.filter
        ?: SUBREDDIT_FILTER_DATE_UPDATED
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getOrderSubreddit(): String {
    return getCurrentViewStateOrNew().subredditFields.order
        ?: SUBREDDIT_ORDER_DESC
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getSearchQuerySubreddit(): String {
    return getCurrentViewStateOrNew().subredditFields.searchQuery
        ?: return ""
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getPageSubreddit(): Int {
    return getCurrentViewStateOrNew().subredditFields.page
        ?: return 1
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.isAuthorOfSubreddit(): Boolean {
    return getCurrentViewStateOrNew().viewSubredditFields.isAuthorOfSubreddit
        ?: false
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getSubreddit(): SubredditRoom {
    getCurrentViewStateOrNew().let {
        return it.viewSubredditFields.subredditRoom?.let {
            return it
        } ?: getDummySubreddit()
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getDummySubreddit(): SubredditRoom {
    return SubredditRoom(-1, "", -1,"", listOf(), "")
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getIsQueryExhaustedSubuser(): Boolean {
    return getCurrentViewStateOrNew().subuserFields.isQueryExhausted
        ?: false
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getFilterSubuser(): String {
    return getCurrentViewStateOrNew().subuserFields.filter
        ?: SUBUSER_FILTER_DATE_UPDATED
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getOrderSubuser(): String {
    return getCurrentViewStateOrNew().subuserFields.order
        ?: SUBUSER_ORDER_DESC
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getSearchQuerySubuser(): String {
    return getCurrentViewStateOrNew().subuserFields.searchQuery
        ?: return ""
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getPageSubuser(): Int {
    return getCurrentViewStateOrNew().subuserFields.page
        ?: return 1
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getAuthorsenderNamePassSubuser(): String {
    return getCurrentViewStateOrNew().subuserFields.authorsenderNamePass
        ?: return ""
}


/* USERLOANREQUEST */


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getIsQueryExhaustedUserloanrequest(): Boolean {
    return getCurrentViewStateOrNew().userloanrequestFields.isQueryExhausted
        ?: false
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getFilterUserloanrequest(): String {
    return getCurrentViewStateOrNew().userloanrequestFields.filter
        ?: USERLOANREQUEST_FILTER_DATE_UPDATED
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getOrderUserloanrequest(): String {
    return getCurrentViewStateOrNew().userloanrequestFields.order
        ?: USERLOANREQUEST_ORDER_DESC
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getSearchQueryUserloanrequest(): String {
    return getCurrentViewStateOrNew().userloanrequestFields.searchQuery
        ?: return ""
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getPageUserloanrequest(): Int {
    return getCurrentViewStateOrNew().userloanrequestFields.page
        ?: return 1
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.isAuthorOfUserloanrequest(): Boolean {
    return getCurrentViewStateOrNew().viewUserloanrequestFields.isAuthorOfUserloanrequest
        ?: false
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getUserloanrequest(): UserloanrequestRoom {
    getCurrentViewStateOrNew().let {
        return it.viewUserloanrequestFields.userloanrequestRoom?.let {
            return it
        } ?: getDummyUserloanrequest()
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getDummyUserloanrequest(): UserloanrequestRoom {
    return UserloanrequestRoom(pk = -1, title = "", body = "",  loanamount= -1, authorsender = "", subreddit = "", albumId = "")
}

fun RootViewModel.getUserloanrequestPk(): String {
    getCurrentViewStateOrNew().let {
        return it.viewUserloanrequestFields.userloanrequestRoom?.let {
            return it.pk.toString()
        } ?: ""
    }
}


/* end USERLOANREQUEST */

/* USERSAVINGREQUEST */


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getIsQueryExhaustedUsersavingrequest(): Boolean {
    return getCurrentViewStateOrNew().usersavingrequestFields.isQueryExhausted
        ?: false
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getFilterUsersavingrequest(): String {
    return getCurrentViewStateOrNew().usersavingrequestFields.filter
        ?: RootQueryUtils.USERSAVINGREQUEST_FILTER_DATE_UPDATED
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getOrderUsersavingrequest(): String {
    return getCurrentViewStateOrNew().usersavingrequestFields.order
        ?: RootQueryUtils.USERSAVINGREQUEST_ORDER_DESC
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getSearchQueryUsersavingrequest(): String {
    return getCurrentViewStateOrNew().usersavingrequestFields.searchQuery
        ?: return ""
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getPageUsersavingrequest(): Int {
    return getCurrentViewStateOrNew().usersavingrequestFields.page
        ?: return 1
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.isAuthorOfUsersavingrequest(): Boolean {
    return getCurrentViewStateOrNew().viewUsersavingrequestFields.isAuthorOfUsersavingrequest
        ?: false
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getUsersavingrequest(): UsersavingrequestRoom {
    getCurrentViewStateOrNew().let {
        return it.viewUsersavingrequestFields.usersavingrequestRoom?.let {
            return it
        } ?: getDummyUsersavingrequest()
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.getDummyUsersavingrequest(): UsersavingrequestRoom {
    return UsersavingrequestRoom(pk = -1, title = "", body = "", savingamount = -1, authorsender = "", subreddit = "", albumId = "")
}

fun RootViewModel.getUsersavingrequestPk(): String {
    getCurrentViewStateOrNew().let {
        return it.viewUsersavingrequestFields.usersavingrequestRoom?.let {
            return it.pk.toString()
        } ?: ""
    }
}


/* end USERSAVINGREQUEST */
