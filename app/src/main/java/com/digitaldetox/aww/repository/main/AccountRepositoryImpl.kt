package com.digitaldetox.aww.repository.main

import android.util.Log
import com.digitaldetox.aww.api.GenericResponse
import com.digitaldetox.aww.api.main.OpenApiMainService
import com.digitaldetox.aww.di.main.MainScope
import com.digitaldetox.aww.models.AccountProperties
import com.digitaldetox.aww.models.AuthToken
import com.digitaldetox.aww.persistence.AccountPropertiesDao
import com.digitaldetox.aww.persistence.SubredditDao
import com.digitaldetox.aww.persistence.UserloanrequestDao
import com.digitaldetox.aww.persistence.UsersavingrequestDao
import com.digitaldetox.aww.repository.NetworkBoundResource
import com.digitaldetox.aww.repository.safeApiCall
import com.digitaldetox.aww.session.SessionManager
import com.digitaldetox.aww.ui.main.account.state.AccountViewState
import com.digitaldetox.aww.util.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@FlowPreview
@MainScope
class AccountRepositoryImpl
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val accountPropertiesDao: AccountPropertiesDao,
    val subredditDao: SubredditDao,
    val userloanrequestDao: UserloanrequestDao,
    val usersavingrequestDao: UsersavingrequestDao,

    val sessionManager: SessionManager,
    val executors: AppExecutors
) : AccountRepository {

    private val TAG: String = "AppDebug"

    override fun getAccountProperties(
        authToken: AuthToken,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>> {
        return object :
            NetworkBoundResource<AccountProperties, AccountProperties, AccountViewState>(
                dispatcher = IO,
                stateEvent = stateEvent,
                apiCall = {
                    openApiMainService
                        .getAccountProperties("Token ${authToken.token!!}")
                },
                cacheCall = {
                    accountPropertiesDao.searchByPk(authToken.account_pk!!)
                }

            ) {
            override suspend fun updateCache(networkObject: AccountProperties) {
                Log.d(TAG, "updateCache: ${networkObject} ")
                accountPropertiesDao.updateAccountProperties(
                    networkObject.pk,
                    networkObject.email,
                    networkObject.username,
                    networkObject.first_name,
                    networkObject.last_name,
                    networkObject.location,
                    networkObject.age,
                    networkObject.savingtarget

                )
            }

            override fun handleCacheSuccess(
                resultObj: AccountProperties
            ): DataState<AccountViewState> {
                return DataState.data(
                    response = null,
                    data = AccountViewState(
                        accountProperties = resultObj
                    ),
                    stateEvent = stateEvent
                )
            }

        }.result
    }

    override fun saveAccountProperties(
        authToken: AuthToken,
        email: String,
        username: String,
        first_name: String,
        last_name: String,
        location: String,
        age: Int,
        savingtarget: Int,
        stateEvent: StateEvent
    ) = flow {
        val apiResult = safeApiCall(IO) {
            openApiMainService.saveAccountProperties(
                "Token ${authToken.token!!}",
                email,
                username,
                first_name,
                last_name,
                location,
                age,
                savingtarget
            )
        }
        emit(
            object : ApiResponseHandler<AccountViewState, GenericResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(
                    resultObj: GenericResponse
                ): DataState<AccountViewState> {

                    val updatedAccountProperties = openApiMainService
                        .getAccountProperties("Token ${authToken.token!!}")

                    accountPropertiesDao.updateAccountProperties(
                        pk = updatedAccountProperties.pk,
                        email = updatedAccountProperties.email,
                        username = updatedAccountProperties.username,
                        first_name = updatedAccountProperties.first_name,
                        last_name = updatedAccountProperties.last_name,
                        location = updatedAccountProperties.location,
                        age = updatedAccountProperties.age,
                        savingtarget = updatedAccountProperties.age

                    )

                    return DataState.data(
                        data = null,
                        response = Response(
                            message = resultObj.response,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        stateEvent = stateEvent
                    )
                }

            }.getResult()
        )
    }

    override fun updatePassword(
        authToken: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
        stateEvent: StateEvent
    ) = flow {
        val apiResult = safeApiCall(IO) {
            openApiMainService.updatePassword(
                "Token ${authToken.token!!}",
                currentPassword,
                newPassword,
                confirmNewPassword
            )
        }
        emit(
            object : ApiResponseHandler<AccountViewState, GenericResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(
                    resultObj: GenericResponse
                ): DataState<AccountViewState> {

                    return DataState.data(
                        data = null,
                        response = Response(
                            message = resultObj.response,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        stateEvent = stateEvent
                    )
                }
            }.getResult()
        )
    }

    override fun cleanUpDeleteAfterLogout() {
        executors.diskIO().execute {
            Log.d(TAG, "cleanUpDeleteAfterLogout: EXEC")
            subredditDao.deleteAllSubreddits()
            userloanrequestDao.deleteAllUserloanrequests()
            usersavingrequestDao.deleteAllUsersavingrequests()

        }
    }


}
















