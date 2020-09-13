package com.digitaldetox.aww.repository.main

import com.digitaldetox.aww.di.main.MainScope
import com.digitaldetox.aww.models.AuthToken
import com.digitaldetox.aww.models.SubredditRoom
import com.digitaldetox.aww.models.UserloanrequestRoom
import com.digitaldetox.aww.models.UsersavingrequestRoom
import com.digitaldetox.aww.ui.main.root.state.RootViewState
import com.digitaldetox.aww.util.DataState
import com.digitaldetox.aww.util.StateEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@MainScope
interface RootRepository {

    fun searchHumansavingprofiles(
        username: String,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>>


    fun searchHumanloanprofiles(
        username: String,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>>


    fun searchSubreddits(
        authToken: AuthToken,
        query: String,
        username: String,
        filterAndOrder: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>>

    fun isAuthorOfSubreddit(
        authToken: AuthToken,
        slug: String,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>>

    fun deleteSubreddit(
        authToken: AuthToken,
        subredditRoom: SubredditRoom,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>>

    fun updateSubreddit(
        authToken: AuthToken,
        subreddit: String,
        title: String,
        description: String,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>>

    fun searchUserloanrequests(
        authToken: AuthToken,
        query: String,
        authorsender: String,
        filterAndOrder: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>>

    fun deleteUserloanrequest(
        authToken: AuthToken,
        userloanrequestRoom: UserloanrequestRoom,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>>

    fun updateUserloanrequest(
        authToken: AuthToken,
        loanrequestPk: String,
        title: String,
        body: String,
        loanamount: Int,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>>

    fun searchUsersavingrequests(
        authToken: AuthToken,
        query: String,
        authorsender: String,
        filterAndOrder: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>>

    fun deleteUsersavingrequest(
        authToken: AuthToken,
        usersavingrequestRoom: UsersavingrequestRoom,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>>

    fun updateUsersavingrequest(
        authToken: AuthToken,
        savingrequestPk: String,
        title: String,
        body: String,
        savingamount: Int,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>>


}