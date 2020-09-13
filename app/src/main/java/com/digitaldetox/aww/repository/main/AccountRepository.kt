package com.digitaldetox.aww.repository.main

import com.digitaldetox.aww.di.main.MainScope
import com.digitaldetox.aww.models.AuthToken
import com.digitaldetox.aww.ui.main.account.state.AccountViewState
import com.digitaldetox.aww.util.DataState
import com.digitaldetox.aww.util.StateEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@MainScope
interface AccountRepository {

    fun getAccountProperties(
        authToken: AuthToken,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>

    fun saveAccountProperties(
        authToken: AuthToken,
        email: String,
        username: String,
        first_name: String,
        last_name: String,
        location: String,
        age: Int,
        savingtarget: Int,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>

    fun updatePassword(
        authToken: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>

    fun cleanUpDeleteAfterLogout()
}