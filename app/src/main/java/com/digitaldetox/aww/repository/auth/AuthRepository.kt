package com.digitaldetox.aww.repository.auth

import com.digitaldetox.aww.di.auth.AuthScope
import com.digitaldetox.aww.ui.auth.state.AuthViewState
import com.digitaldetox.aww.util.DataState
import com.digitaldetox.aww.util.StateEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@AuthScope
interface AuthRepository {

    fun attemptLogin(
        stateEvent: StateEvent,
        username: String,
        password: String
    ): Flow<DataState<AuthViewState>>

    fun attemptRegistration(
        stateEvent: StateEvent,
        email: String,
        username: String,
        first_name: String,
        last_name: String,
        location: String,
        aadharcard: String,
        age: Int,
        savingtarget: Int,
        password: String
    ): Flow<DataState<AuthViewState>>

    fun checkPreviousAuthUser(
        stateEvent: StateEvent
    ): Flow<DataState<AuthViewState>>

    fun saveAuthenticatedUserToPrefs(username: String)

    fun returnNoTokenFound(
        stateEvent: StateEvent
    ): DataState<AuthViewState>

}
