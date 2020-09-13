package com.digitaldetox.aww.ui.main.root.viewmodel

import android.net.Uri
import android.os.Parcelable
import com.digitaldetox.aww.models.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setHumansavingprofileListData(humansavingprofileList: List<HumansavingprofileRoom>) {
    val update = getCurrentViewStateOrNew()
    update.humansavingprofileFields.humansavingprofileList = humansavingprofileList
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setHumansavingprofileFilterHumansavingprofile(filter: String?) {
    filter?.let {
        val update = getCurrentViewStateOrNew()
        update.humansavingprofileFields.filter = filter
        setViewState(update)
    }
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setHumansavingprofileOrderHumansavingprofile(order: String) {
    val update = getCurrentViewStateOrNew()
    update.humansavingprofileFields.order = order
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setLayoutManagerStateHumansavingprofile(layoutManagerState: Parcelable) {
    val update = getCurrentViewStateOrNew()
    update.humansavingprofileFields.layoutManagerState = layoutManagerState
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.clearLayoutManagerStateHumansavingprofile() {
    val update = getCurrentViewStateOrNew()
    update.humansavingprofileFields.layoutManagerState = null
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setQueryExhaustedHumansavingprofile(isExhausted: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.humansavingprofileFields.isQueryExhausted = isExhausted
    setViewState(update)
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setHumanloanprofileListData(humanloanprofileList: List<HumanloanprofileRoom>) {
    val update = getCurrentViewStateOrNew()
    update.humanloanprofileFields.humanloanprofileList = humanloanprofileList
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setHumanloanprofileFilterHumanloanprofile(filter: String?) {
    filter?.let {
        val update = getCurrentViewStateOrNew()
        update.humanloanprofileFields.filter = filter
        setViewState(update)
    }
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setHumanloanprofileOrderHumanloanprofile(order: String) {
    val update = getCurrentViewStateOrNew()
    update.humanloanprofileFields.order = order
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setLayoutManagerStateHumanloanprofile(layoutManagerState: Parcelable) {
    val update = getCurrentViewStateOrNew()
    update.humanloanprofileFields.layoutManagerState = layoutManagerState
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.clearLayoutManagerStateHumanloanprofile() {
    val update = getCurrentViewStateOrNew()
    update.humanloanprofileFields.layoutManagerState = null
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setQueryExhaustedHumanloanprofile(isExhausted: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.humanloanprofileFields.isQueryExhausted = isExhausted
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setQuerySubreddit(query: String) {
    val update = getCurrentViewStateOrNew()
    update.subredditFields.searchQuery = query
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setSubredditListData(subredditList: List<SubredditRoom>) {
    val update = getCurrentViewStateOrNew()
    update.subredditFields.subredditList = subredditList
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setSubreddit(subredditRoom: SubredditRoom) {
    val update = getCurrentViewStateOrNew()
    update.viewSubredditFields.subredditRoom = subredditRoom
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setIsAuthorOfSubreddit(isAuthorOfSubreddit: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.viewSubredditFields.isAuthorOfSubreddit = isAuthorOfSubreddit
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setQueryExhaustedSubreddit(isExhausted: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.subredditFields.isQueryExhausted = isExhausted
    setViewState(update)
}



@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setSubredditFilterSubreddit(filter: String?) {
    filter?.let {
        val update = getCurrentViewStateOrNew()
        update.subredditFields.filter = filter
        setViewState(update)
    }
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setSubredditOrderSubreddit(order: String) {
    val update = getCurrentViewStateOrNew()
    update.subredditFields.order = order
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setLayoutManagerStateSubreddit(layoutManagerState: Parcelable) {
    val update = getCurrentViewStateOrNew()
    update.subredditFields.layoutManagerState = layoutManagerState
    setViewState(update)
}



@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.clearLayoutManagerStateSubreddit() {
    val update = getCurrentViewStateOrNew()
    update.subredditFields.layoutManagerState = null
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.removeDeletedSubreddit() {
    val update = getCurrentViewStateOrNew()
    val list = update.subredditFields.subredditList?.toMutableList()
    if (list != null) {
        for (i in 0..(list.size - 1)) {
            if (list[i] == getSubreddit()) {
                list.remove(getSubreddit())
                break
            }
        }
        setSubredditListData(list)
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.updateListItemSubreddit() {
    val update = getCurrentViewStateOrNew()
    val list = update.subredditFields.subredditList?.toMutableList()
    if (list != null) {
        val newSubreddit = getSubreddit()
        for (i in 0..(list.size - 1)) {
            if (list[i].pk == newSubreddit.pk) {
                list[i] = newSubreddit
                break
            }
        }
        update.subredditFields.subredditList = list
        setViewState(update)
    }
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUpdatedUriSubreddit(uri: Uri) {
    val update = getCurrentViewStateOrNew()
    val updatedSubredditFields = update.updatedSubredditFields
    update.updatedSubredditFields = updatedSubredditFields
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUpdatedTitleSubreddit(title: String) {
    val update = getCurrentViewStateOrNew()
    val updatedSubredditFields = update.updatedSubredditFields
    updatedSubredditFields.updatedSubredditTitle = title
    update.updatedSubredditFields = updatedSubredditFields
    setViewState(update)
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUpdatedBodySubreddit(body: String) {
    val update = getCurrentViewStateOrNew()
    val updatedSubredditFields = update.updatedSubredditFields
    updatedSubredditFields.updatedSubredditBody = body
    update.updatedSubredditFields = updatedSubredditFields
    setViewState(update)
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setQuerySubuser(query: String) {
    val update = getCurrentViewStateOrNew()
    update.subuserFields.searchQuery = query
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setSubuserListData(subuserList: List<String>) {
    val update = getCurrentViewStateOrNew()
    update.subuserFields.subuserList = subuserList
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setQueryExhaustedSubuser(isExhausted: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.subuserFields.isQueryExhausted = isExhausted
    setViewState(update)
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setSubuserFilterSubuser(filter: String?) {
    filter?.let {
        val update = getCurrentViewStateOrNew()
        update.subuserFields.filter = filter
        setViewState(update)
    }
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setSubuserOrderSubuser(order: String) {
    val update = getCurrentViewStateOrNew()
    update.subuserFields.order = order
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setLayoutManagerStateSubuser(layoutManagerState: Parcelable) {
    val update = getCurrentViewStateOrNew()
    update.subuserFields.layoutManagerState = layoutManagerState
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.clearLayoutManagerStateSubuser() {
    val update = getCurrentViewStateOrNew()
    update.subuserFields.layoutManagerState = null
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setSubuserPassAuthorsenderName(authorsenderName: String) {
    val update = getCurrentViewStateOrNew()
    update.subuserFields.authorsenderNamePass = authorsenderName
    setViewState(update)
}


/* USERLOANREQUEST */

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setQueryUserloanrequest(query: String) {
    val update = getCurrentViewStateOrNew()
    update.userloanrequestFields.searchQuery = query
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUserloanrequestListData(userloanrequestList: List<UserloanrequestRoom>) {
    val update = getCurrentViewStateOrNew()
    update.userloanrequestFields.userloanrequestList = userloanrequestList
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUserloanrequest(userloanrequestRoom: UserloanrequestRoom) {
    val update = getCurrentViewStateOrNew()
    update.viewUserloanrequestFields.userloanrequestRoom = userloanrequestRoom
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setIsAuthorOfUserloanrequest(isAuthorOfUserloanrequest: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.viewUserloanrequestFields.isAuthorOfUserloanrequest = isAuthorOfUserloanrequest
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setQueryExhaustedUserloanrequest(isExhausted: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.userloanrequestFields.isQueryExhausted = isExhausted
    setViewState(update)
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUserloanrequestFilterUserloanrequest(filter: String?) {
    filter?.let {
        val update = getCurrentViewStateOrNew()
        update.userloanrequestFields.filter = filter
        setViewState(update)
    }
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUserloanrequestOrderUserloanrequest(order: String) {
    val update = getCurrentViewStateOrNew()
    update.userloanrequestFields.order = order
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setLayoutManagerStateUserloanrequest(layoutManagerState: Parcelable) {
    val update = getCurrentViewStateOrNew()
    update.userloanrequestFields.layoutManagerState = layoutManagerState
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.clearLayoutManagerStateUserloanrequest() {
    val update = getCurrentViewStateOrNew()
    update.userloanrequestFields.layoutManagerState = null
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.removeDeletedUserloanrequest() {
    val update = getCurrentViewStateOrNew()
    val list = update.userloanrequestFields.userloanrequestList?.toMutableList()
    if (list != null) {
        for (i in 0..(list.size - 1)) {
            if (list[i] == getUserloanrequest()) {
                list.remove(getUserloanrequest())
                break
            }
        }
        setUserloanrequestListData(list)
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.updateListItemUserloanrequest() {
    val update = getCurrentViewStateOrNew()
    val list = update.userloanrequestFields.userloanrequestList?.toMutableList()
    if (list != null) {
        val newUserloanrequest = getUserloanrequest()
        for (i in 0..(list.size - 1)) {
            if (list[i].pk == newUserloanrequest.pk) {
                list[i] = newUserloanrequest
                break
            }
        }
        update.userloanrequestFields.userloanrequestList = list
        setViewState(update)
    }
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUpdatedUriUserloanrequest(uri: Uri) {
    val update = getCurrentViewStateOrNew()
    val updatedUserloanrequestFields = update.updatedUserloanrequestFields
    update.updatedUserloanrequestFields = updatedUserloanrequestFields
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUpdatedTitleUserloanrequest(title: String) {
    val update = getCurrentViewStateOrNew()
    val updatedUserloanrequestFields = update.updatedUserloanrequestFields
    updatedUserloanrequestFields.updatedUserloanrequestTitle = title
    update.updatedUserloanrequestFields = updatedUserloanrequestFields
    setViewState(update)
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUpdatedBodyUserloanrequest(body: String) {
    val update = getCurrentViewStateOrNew()
    val updatedUserloanrequestFields = update.updatedUserloanrequestFields
    updatedUserloanrequestFields.updatedUserloanrequestBody = body
    update.updatedUserloanrequestFields = updatedUserloanrequestFields
    setViewState(update)
}
@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUpdatedLoanamountUserloanrequest(loanamount: Int) {
    val update = getCurrentViewStateOrNew()
    val updatedUserloanrequestFields = update.updatedUserloanrequestFields
    updatedUserloanrequestFields.updatedUserloanrequestLoanamount = loanamount
    update.updatedUserloanrequestFields = updatedUserloanrequestFields
    setViewState(update)
}

/* end USERLOANREQUEST */

/* USERSAVINGREQUEST */

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setQueryUsersavingrequest(query: String) {
    val update = getCurrentViewStateOrNew()
    update.usersavingrequestFields.searchQuery = query
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUsersavingrequestListData(usersavingrequestList: List<UsersavingrequestRoom>) {
    val update = getCurrentViewStateOrNew()
    update.usersavingrequestFields.usersavingrequestList = usersavingrequestList
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUsersavingrequest(usersavingrequestRoom: UsersavingrequestRoom) {
    val update = getCurrentViewStateOrNew()
    update.viewUsersavingrequestFields.usersavingrequestRoom = usersavingrequestRoom
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setIsAuthorOfUsersavingrequest(isAuthorOfUsersavingrequest: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.viewUsersavingrequestFields.isAuthorOfUsersavingrequest = isAuthorOfUsersavingrequest
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setQueryExhaustedUsersavingrequest(isExhausted: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.usersavingrequestFields.isQueryExhausted = isExhausted
    setViewState(update)
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUsersavingrequestFilterUsersavingrequest(filter: String?) {
    filter?.let {
        val update = getCurrentViewStateOrNew()
        update.usersavingrequestFields.filter = filter
        setViewState(update)
    }
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUsersavingrequestOrderUsersavingrequest(order: String) {
    val update = getCurrentViewStateOrNew()
    update.usersavingrequestFields.order = order
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setLayoutManagerStateUsersavingrequest(layoutManagerState: Parcelable) {
    val update = getCurrentViewStateOrNew()
    update.usersavingrequestFields.layoutManagerState = layoutManagerState
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.clearLayoutManagerStateUsersavingrequest() {
    val update = getCurrentViewStateOrNew()
    update.usersavingrequestFields.layoutManagerState = null
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.removeDeletedUsersavingrequest() {
    val update = getCurrentViewStateOrNew()
    val list = update.usersavingrequestFields.usersavingrequestList?.toMutableList()
    if (list != null) {
        for (i in 0..(list.size - 1)) {
            if (list[i] == getUsersavingrequest()) {
                list.remove(getUsersavingrequest())
                break
            }
        }
        setUsersavingrequestListData(list)
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.updateListItemUsersavingrequest() {
    val update = getCurrentViewStateOrNew()
    val list = update.usersavingrequestFields.usersavingrequestList?.toMutableList()
    if (list != null) {
        val newUsersavingrequest = getUsersavingrequest()
        for (i in 0..(list.size - 1)) {
            if (list[i].pk == newUsersavingrequest.pk) {
                list[i] = newUsersavingrequest
                break
            }
        }
        update.usersavingrequestFields.usersavingrequestList = list
        setViewState(update)
    }
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUpdatedUriUsersavingrequest(uri: Uri) {
    val update = getCurrentViewStateOrNew()
    val updatedUsersavingrequestFields = update.updatedUsersavingrequestFields
    update.updatedUsersavingrequestFields = updatedUsersavingrequestFields
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUpdatedTitleUsersavingrequest(title: String) {
    val update = getCurrentViewStateOrNew()
    val updatedUsersavingrequestFields = update.updatedUsersavingrequestFields
    updatedUsersavingrequestFields.updatedUsersavingrequestTitle = title
    update.updatedUsersavingrequestFields = updatedUsersavingrequestFields
    setViewState(update)
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUpdatedBodyUsersavingrequest(body: String) {
    val update = getCurrentViewStateOrNew()
    val updatedUsersavingrequestFields = update.updatedUsersavingrequestFields
    updatedUsersavingrequestFields.updatedUsersavingrequestBody = body
    update.updatedUsersavingrequestFields = updatedUsersavingrequestFields
    setViewState(update)
}



@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.setUpdatedSavingamountUsersavingrequest(savingamount: Int) {
    val update = getCurrentViewStateOrNew()
    val updatedUsersavingrequestFields = update.updatedUsersavingrequestFields
    updatedUsersavingrequestFields.updatedUsersavingrequestSavingamount = savingamount
    update.updatedUsersavingrequestFields = updatedUsersavingrequestFields
    setViewState(update)
}
/* end USERSAVINGREQUEST */