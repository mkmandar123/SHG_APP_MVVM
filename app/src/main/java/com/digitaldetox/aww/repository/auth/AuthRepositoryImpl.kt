package com.digitaldetox.aww.repository.auth

import android.content.SharedPreferences
import android.util.Log
import com.digitaldetox.aww.api.auth.OpenApiAuthService
import com.digitaldetox.aww.api.auth.network_responses.LoginResponse
import com.digitaldetox.aww.api.auth.network_responses.RegistrationResponse
import com.digitaldetox.aww.di.auth.AuthScope
import com.digitaldetox.aww.models.AccountProperties
import com.digitaldetox.aww.models.AuthToken
import com.digitaldetox.aww.persistence.AccountPropertiesDao
import com.digitaldetox.aww.persistence.AuthTokenDao
import com.digitaldetox.aww.repository.buildError
import com.digitaldetox.aww.repository.safeApiCall
import com.digitaldetox.aww.repository.safeCacheCall
import com.digitaldetox.aww.session.SessionManager
import com.digitaldetox.aww.ui.auth.state.AuthViewState
import com.digitaldetox.aww.ui.auth.state.LoginFields
import com.digitaldetox.aww.ui.auth.state.RegistrationFields
import com.digitaldetox.aww.util.*
import com.digitaldetox.aww.util.ErrorHandling.Companion.ERROR_SAVE_ACCOUNT_PROPERTIES
import com.digitaldetox.aww.util.ErrorHandling.Companion.ERROR_SAVE_AUTH_TOKEN
import com.digitaldetox.aww.util.ErrorHandling.Companion.GENERIC_AUTH_ERROR
import com.digitaldetox.aww.util.ErrorHandling.Companion.INVALID_CREDENTIALS
import com.digitaldetox.aww.util.SuccessHandling.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@FlowPreview
@AuthScope
class AuthRepositoryImpl
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager,
    val sharedPreferences: SharedPreferences,
    val sharedPrefsEditor: SharedPreferences.Editor
) : AuthRepository {

    private val TAG: String = "AppDebug"

    override fun attemptLogin(
        stateEvent: StateEvent,
        username: String,
        password: String
    ): Flow<DataState<AuthViewState>> = flow {

        val loginFieldErrors = LoginFields(username, password).isValidForLogin()
        if (loginFieldErrors.equals(LoginFields.LoginError.none())) {
            val apiResult = safeApiCall(IO) {
                openApiAuthService.login(username, password)
            }
            emit(
                object : ApiResponseHandler<AuthViewState, LoginResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: LoginResponse): DataState<AuthViewState> {

                        Log.d(TAG, "handleSuccess ")

                        // Incorrect login credentials counts as a 200 response from server, so need to handle that
                        if (resultObj.response_server.equals(GENERIC_AUTH_ERROR)) {
                            return DataState.error(
                                response = Response(
                                    INVALID_CREDENTIALS,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        accountPropertiesDao.insertOrIgnore(
                            AccountProperties(
                                resultObj.pk,
                                resultObj.email,
                                resultObj.username,
                                resultObj.first_name,
                                resultObj.last_name,
                                resultObj.location,
                                resultObj.aadharcard,
                                resultObj.age,
                                resultObj.savingtarget

                            )
                        )

                        // will return -1 if failure
                        val authToken = AuthToken(
                            resultObj.pk,
                            resultObj.token
                        )
                        val result = authTokenDao.insert(authToken)
                        if (result < 0) {
                            return DataState.error(
                                response = Response(
                                    ERROR_SAVE_AUTH_TOKEN,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        saveAuthenticatedUserToPrefs(username)

                        return DataState.data(
                            data = AuthViewState(
                                authToken = authToken
                            ),
                            stateEvent = stateEvent,
                            response = null
                        )
                    }

                }.getResult()
            )
        } else {
            Log.d(TAG, "emitting error: ${loginFieldErrors}")
            emit(
                buildError(
                    loginFieldErrors,
                    UIComponentType.Dialog(),
                    stateEvent
                )
            )
        }
    }

    override fun attemptRegistration(
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
    ): Flow<DataState<AuthViewState>> = flow {
        val registrationFieldErrors = RegistrationFields(
            registration_email = email,
            registration_username = username,
            registration_firstname = first_name,
            registration_lastname = last_name,
            registration_location = location,
            registration_aadharcard = aadharcard,
            registration_age = age,
            registration_savingtarget = savingtarget,
            registration_password = password
        ).isValidForRegistration()
        if (registrationFieldErrors.equals(RegistrationFields.RegistrationError.none())) {

            val apiResult = safeApiCall(IO) {
                openApiAuthService.register(
                    email = email,
                    username = username,
                    first_name = first_name,
                    last_name = last_name,
                    location = location,
                    aadharcard = aadharcard,
                    age = age,
                    password = password,
                    savingtarget = savingtarget
                )
            }
            emit(
                object : ApiResponseHandler<AuthViewState, RegistrationResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: RegistrationResponse): DataState<AuthViewState> {
                        if (resultObj?.response_registration_server == GENERIC_AUTH_ERROR) {
                            return DataState.error(
                                response = Response(
                                    resultObj.errorMessage,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        val result1 = accountPropertiesDao.insertAndReplace(
                            AccountProperties(
                                resultObj.pk,
                                resultObj.email,
                                resultObj.username,
                                resultObj.first_name,
                                resultObj.last_name,
                                resultObj.location,
                                resultObj.aadharcard,
                                resultObj.age,
                                resultObj.savingtarget
                            )
                        )
                        // will return -1 if failure
                        if (result1 < 0) {
                            return DataState.error(
                                response = Response(
                                    ERROR_SAVE_ACCOUNT_PROPERTIES,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }

                        // will return -1 if failure
                        val authToken = AuthToken(
                            resultObj.pk,
                            resultObj.token
                        )
                        val result2 = authTokenDao.insert(authToken)
                        if (result2 < 0) {
                            return DataState.error(
                                response = Response(
                                    ERROR_SAVE_AUTH_TOKEN,
                                    UIComponentType.Dialog(),
                                    MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        saveAuthenticatedUserToPrefs(username)
                        return DataState.data(
                            data = AuthViewState(
                                authToken = authToken
                            ),
                            stateEvent = stateEvent,
                            response = null
                        )
                    }
                }.getResult()
            )

        } else {
            emit(
                buildError(
                    registrationFieldErrors,
                    UIComponentType.Dialog(),
                    stateEvent
                )
            )
        }

    }


    override fun checkPreviousAuthUser(
        stateEvent: StateEvent
    ): Flow<DataState<AuthViewState>> = flow {
        Log.d(TAG, "checkPreviousAuthUser: ")
//        val previousAuthUserEmail: String? = sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null)
        val previousAuthUserUsername: String? =
            sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null)

        if (previousAuthUserUsername.isNullOrBlank()) {
            Log.d(TAG, "checkPreviousAuthUser: No previously authenticated user found.")
            emit(returnNoTokenFound(stateEvent))
        } else {
            val apiResult = safeCacheCall(IO) {
                accountPropertiesDao.searchByUsername(previousAuthUserUsername)
            }
            emit(
                object : CacheResponseHandler<AuthViewState, AccountProperties>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: AccountProperties): DataState<AuthViewState> {

                        if (resultObj.pk > -1) {
                            authTokenDao.searchByPk(resultObj.pk).let { authToken ->
                                if (authToken != null) {
                                    if (authToken.token != null) {
                                        return DataState.data(
                                            data = AuthViewState(
                                                authToken = authToken
                                            ),
                                            response = null,
                                            stateEvent = stateEvent
                                        )
                                    }
                                }
                            }
                        }
                        Log.d(TAG, "createCacheRequestAndReturn: AuthToken not found...")
                        return DataState.error(
                            response = Response(
                                RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                                UIComponentType.None(),
                                MessageType.Error()
                            ),
                            stateEvent = stateEvent
                        )
                    }
                }.getResult()
            )
        }
    }

    override fun saveAuthenticatedUserToPrefs(username: String) {
        sharedPrefsEditor.putString(PreferenceKeys.PREVIOUS_AUTH_USER, username)
        sharedPrefsEditor.apply()
    }

    override fun returnNoTokenFound(
        stateEvent: StateEvent
    ): DataState<AuthViewState> {

        return DataState.error(
            response = Response(
                RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                UIComponentType.None(),
                MessageType.Error()
            ),
            stateEvent = stateEvent
        )
    }


}





