package com.digitaldetox.aww.repository.main


import android.util.Log
import com.digitaldetox.aww.api.GenericResponse
import com.digitaldetox.aww.api.main.OpenApiMainService
import com.digitaldetox.aww.api.main.responses.*
import com.digitaldetox.aww.di.main.MainScope
import com.digitaldetox.aww.models.*
import com.digitaldetox.aww.persistence.*
import com.digitaldetox.aww.repository.NetworkBoundResource
import com.digitaldetox.aww.repository.buildError
import com.digitaldetox.aww.repository.safeApiCall
import com.digitaldetox.aww.session.SessionManager
import com.digitaldetox.aww.ui.main.root.state.RootViewState
import com.digitaldetox.aww.ui.main.root.state.RootViewState.*
import com.digitaldetox.aww.util.*
import com.digitaldetox.aww.util.ErrorHandling.Companion.ERROR_UNKNOWN
import com.digitaldetox.aww.util.SuccessHandling.Companion.RESPONSE_HAS_PERMISSION_TO_EDIT
import com.digitaldetox.aww.util.SuccessHandling.Companion.RESPONSE_NO_PERMISSION_TO_EDIT
import com.digitaldetox.aww.util.SuccessHandling.Companion.SUCCESS_SUBREDDIT_DELETED
import com.digitaldetox.aww.util.SuccessHandling.Companion.SUCCESS_USERLOANREQUEST_DELETED
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@FlowPreview
@MainScope
class RootRepositoryImpl
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val subredditDao: SubredditDao,
    val humanloanprofileDao: HumanloanprofileDao,
    val humansavingprofileDao: HumansavingprofileDao,

    val userloanrequestDao: UserloanrequestDao,
    val usersavingrequestDao: UsersavingrequestDao,

    val sessionManager: SessionManager,
    val executors: AppExecutors
) : RootRepository {

    private val rateLimiter = RateLimiter<String>(30, TimeUnit.SECONDS)

    private val rateLimitKey = "albums"

    private val TAG: String = "AppDebug"

    override fun searchHumansavingprofiles(
        username: String,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>> {
        return object :
            NetworkBoundResource<HumansavingprofileListSearchResponse, List<HumansavingprofileRoom>, RootViewState>(
                dispatcher = Dispatchers.IO,
                stateEvent = stateEvent,
                apiCall = {
                    openApiMainService.searchListHumansavingprofileSubscribedByUser(
                        username = username
                    )
                },
                cacheCall = {
                    humansavingprofileDao.searchHumansavingprofileroomsOrderByDateDESC(

                    )
                }
            ) {


            override fun handleCacheSuccess(resultObj: List<HumansavingprofileRoom>): DataState<RootViewState> {
                val viewState = RootViewState(
                    humansavingprofileFields = RootViewState.HumansavingprofileFields(
                        humansavingprofileList = resultObj
                    )
                )
                return DataState.data(
                    response = null,
                    data = viewState,
                    stateEvent = stateEvent
                )
            }

            override suspend fun updateCache(networkObject: HumansavingprofileListSearchResponse) {
                val humansavingprofileList = networkObject.toList()
                withContext(Dispatchers.IO) {
                    for (humansavingprofile in humansavingprofileList) {
                        try {
                            // Launch each insert as a separate job to be executed in parallel
                            launch {
                                Log.d(TAG, "updateLocalDb: inserting humansavingprofile: ${humansavingprofile}")
                                humansavingprofileDao.insert(humansavingprofile)
                            }
                        } catch (e: Exception) {
                            Log.e(
                                TAG,
                                "updateLocalDb: error updating cache data on humansavingprofile post with slug: ${humansavingprofile.pk}. " +
                                        "${e.message}"
                            )
                            // Could send an error report here or something but I don't think you should throw an error to the UI
                            // Since there could be many humansavingprofile posts being inserted/updated.
                        }
                    }
                }
            }
        }.result
    }

    override fun searchHumanloanprofiles(
        username: String,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>> {
        return object :
            NetworkBoundResource<HumanloanprofileListSearchResponse, List<HumanloanprofileRoom>, RootViewState>(
                dispatcher = Dispatchers.IO,
                stateEvent = stateEvent,
                apiCall = {
                    openApiMainService.searchListHumanloanprofileSubscribedByUser(
                        username = username
                    )
                },
                cacheCall = {
                    humanloanprofileDao.searchHumanloanprofileroomsOrderByDateDESC(

                    )
                }
            ) {


            override fun handleCacheSuccess(resultObj: List<HumanloanprofileRoom>): DataState<RootViewState> {
                val viewState = RootViewState(
                    humanloanprofileFields = HumanloanprofileFields(
                        humanloanprofileList = resultObj
                    )
                )
                return DataState.data(
                    response = null,
                    data = viewState,
                    stateEvent = stateEvent
                )
            }

            override suspend fun updateCache(networkObject: HumanloanprofileListSearchResponse) {
                val humanloanprofileList = networkObject.toList()
                withContext(Dispatchers.IO) {
                    for (humanloanprofile in humanloanprofileList) {
                        try {
                            // Launch each insert as a separate job to be executed in parallel
                            launch {
                                Log.d(TAG, "updateLocalDb: inserting humanloanprofile: ${humanloanprofile}")
                                humanloanprofileDao.insert(humanloanprofile)
                            }
                        } catch (e: Exception) {
                            Log.e(
                                TAG,
                                "updateLocalDb: error updating cache data on humanloanprofile post with slug: ${humanloanprofile.pk}. " +
                                        "${e.message}"
                            )
                            // Could send an error report here or something but I don't think you should throw an error to the UI
                            // Since there could be many humanloanprofile posts being inserted/updated.
                        }
                    }
                }
            }
        }.result
    }

    override fun searchSubreddits(
        authToken: AuthToken,
        query: String,
        username: String,
        filterAndOrder: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>> {
        return object :
            NetworkBoundResource<SubredditListSearchResponse, List<SubredditRoom>, RootViewState>(
                dispatcher = IO,
                stateEvent = stateEvent,
                apiCall = {
                    openApiMainService.searchListSubredditSubscribedByUser(
                        username = username,
                        query = query,
                        ordering = filterAndOrder,
                        page = page
                    )
                },
                cacheCall = {
                    subredditDao.returnOrderedSubredditQuery(
                        query = query,
                        filterAndOrder = filterAndOrder,
                        page = page
                    )
                }
            ) {
            override suspend fun updateCache(networkObject: SubredditListSearchResponse) {
                val subredditList = networkObject.toList()
                withContext(IO) {
                    for (subreddit in subredditList) {
                        try {
                            // Launch each insert as a separate job to be executed in parallel
                            launch {
                                Log.d(TAG, "updateLocalDb: inserting subreddit: ${subreddit}")
                                subredditDao.insert(subreddit)
                            }
                        } catch (e: Exception) {
                            Log.e(
                                TAG,
                                "updateLocalDb: error updating cache data on subreddit post with slug: ${subreddit.pk}. " +
                                        "${e.message}"
                            )
                            // Could send an error report here or something but I don't think you should throw an error to the UI
                            // Since there could be many subreddit posts being inserted/updated.
                        }
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: List<SubredditRoom>): DataState<RootViewState> {
                val viewState = RootViewState(
                    subredditFields = SubredditFields(
                        subredditList = resultObj
                    )
                )
                return DataState.data(
                    response = null,
                    data = viewState,
                    stateEvent = stateEvent
                )
            }

        }.result
    }

    override fun isAuthorOfSubreddit(
        authToken: AuthToken,
        slug: String,
        stateEvent: StateEvent
    ) = flow {
        val apiResult = safeApiCall(IO) {
            openApiMainService.isAuthorOfSubreddit(
                "Token ${authToken.token!!}",
                slug
            )
        }
        emit(
            object : ApiResponseHandler<RootViewState, GenericResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: GenericResponse): DataState<RootViewState> {
                    val viewState = RootViewState(
                        viewSubredditFields = ViewSubredditFields(
                            isAuthorOfSubreddit = false
                        )
                    )
                    return when {

                        resultObj.response.equals(RESPONSE_NO_PERMISSION_TO_EDIT) -> {
                            DataState.data(
                                response = null,
                                data = viewState,
                                stateEvent = stateEvent
                            )
                        }

                        resultObj.response.equals(RESPONSE_HAS_PERMISSION_TO_EDIT) -> {
                            viewState.viewSubredditFields.isAuthorOfSubreddit = true
                            DataState.data(
                                response = null,
                                data = viewState,
                                stateEvent = stateEvent
                            )
                        }

                        else -> {
                            buildError(
                                ERROR_UNKNOWN,
                                UIComponentType.None(),
                                stateEvent
                            )
                        }
                    }
                }
            }.getResult()
        )
    }

    override fun deleteSubreddit(
        authToken: AuthToken,
        subredditRoom: SubredditRoom,
        stateEvent: StateEvent
    ) = flow {
        val apiResult = safeApiCall(IO) {
            openApiMainService.deleteSubreddit(
                "Token ${authToken.token!!}",
                subredditRoom.pk.toString()
            )
        }
        emit(
            object : ApiResponseHandler<RootViewState, GenericResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: GenericResponse): DataState<RootViewState> {

                    if (resultObj.response == SUCCESS_SUBREDDIT_DELETED) {
                        subredditDao.deleteSubredditroom(subredditRoom)
                        return DataState.data(
                            response = Response(
                                message = SUCCESS_SUBREDDIT_DELETED,
                                uiComponentType = UIComponentType.Toast(),
                                messageType = MessageType.Success()
                            ),
                            stateEvent = stateEvent
                        )
                    } else {
                        return buildError(
                            ERROR_UNKNOWN,
                            UIComponentType.Dialog(),
                            stateEvent
                        )
                    }
                }
            }.getResult()
        )
    }

    override fun updateSubreddit(
        authToken: AuthToken,
        subreddit: String,
        title: String,
        description: String,
        stateEvent: StateEvent
    ) = flow {

        val apiResult = safeApiCall(IO) {
            openApiMainService.updateSubreddit(
                "Token ${authToken.token!!}",
                subreddit_name = subreddit,
                title = title,
                description = description

            )
        }
        emit(
            object : ApiResponseHandler<RootViewState, SubredditCreateUpdateResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: SubredditCreateUpdateResponse): DataState<RootViewState> {

                    val updatedSubreddit = resultObj.toSubredditroom()

                    subredditDao.updateSubredditroom(
                        updatedSubreddit.pk,
                        updatedSubreddit.title,
                        updatedSubreddit.description
                    )

                    return DataState.data(
                        response = Response(
                            message = resultObj.title,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = RootViewState(
                            viewSubredditFields = ViewSubredditFields(
                                subredditRoom = updatedSubreddit
                            ),
                            updatedSubredditFields = UpdatedSubredditFields(
                                updatedSubredditTitle = updatedSubreddit.title,
                                updatedSubredditBody = updatedSubreddit.description
                            )
                        ),
                        stateEvent = stateEvent
                    )

                }

            }.getResult()
        )
    }

    override fun searchUserloanrequests(
        authToken: AuthToken,
        query: String,
        authorsender: String,
        filterAndOrder: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>> {
        return object :
            NetworkBoundResource<UserloanrequestListSearchResponse, List<UserloanrequestRoom>, RootViewState>(
                dispatcher = IO,
                stateEvent = stateEvent,
                apiCall = {
                    openApiMainService.searchListUserloanrequest(
                        authorsender = authorsender,
                        query = query,
                        ordering = filterAndOrder,
                        page = page
                    )
                },
                cacheCall = {
                    userloanrequestDao.returnOrderedUserloanrequestQuery(
                        query = query,
                        filterAndOrder = filterAndOrder,
                        page = page,
                        authorsender = authorsender
                    )
                }
            ) {
            override suspend fun updateCache(networkObject: UserloanrequestListSearchResponse) {
                val userloanrequestList = networkObject.toList()
                withContext(IO) {
                    for (userloanrequest in userloanrequestList) {
                        try {
                            // Launch each insert as a separate job to be executed in parallel
                            launch {
                                Log.d(TAG, "updateLocalDb: inserting userloanrequest: ${userloanrequest}")
                                userloanrequestDao.insert(userloanrequest)
                            }
                        } catch (e: Exception) {
                            Log.e(
                                TAG,
                                "updateLocalDb: error updating cache data on userloanrequest post with slug: ${userloanrequest.pk}. " +
                                        "${e.message}"
                            )
                            // Could send an error report here or something but I don't think you should throw an error to the UI
                            // Since there could be many userloanrequest posts being inserted/updated.
                        }
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: List<UserloanrequestRoom>): DataState<RootViewState> {

                Log.d(TAG, "handleCacheSuccess: 327: resultObjuserpostList ${resultObj}")
                
                val viewState = RootViewState(
                    userloanrequestFields = UserloanrequestFields(
                        userloanrequestList = resultObj
                    )
                )
                return DataState.data(
                    response = null,
                    data = viewState,
                    stateEvent = stateEvent
                )
            }

        }.result
    }


    override fun deleteUserloanrequest(
        authToken: AuthToken,
        userloanrequestRoom: UserloanrequestRoom,
        stateEvent: StateEvent
    ) = flow {
        val apiResult = safeApiCall(IO) {
            openApiMainService.deleteUserloanrequest(
                "Token ${authToken.token!!}",
                userloanrequestRoom.pk.toString()
            )
        }
        emit(
            object : ApiResponseHandler<RootViewState, GenericResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: GenericResponse): DataState<RootViewState> {

                    if (resultObj.response == SUCCESS_USERLOANREQUEST_DELETED) {
                        userloanrequestDao.deleteUserloanrequest(userloanrequestRoom)
                        return DataState.data(
                            response = Response(
                                message = SUCCESS_USERLOANREQUEST_DELETED,
                                uiComponentType = UIComponentType.Toast(),
                                messageType = MessageType.Success()
                            ),
                            stateEvent = stateEvent
                        )
                    } else {
                        return buildError(
                            ERROR_UNKNOWN,
                            UIComponentType.Dialog(),
                            stateEvent
                        )
                    }
                }
            }.getResult()
        )
    }

    override fun updateUserloanrequest(
        authToken: AuthToken,
        loanrequestPk: String,
        title: String,
        body: String,
        loanamount: Int,
        stateEvent: StateEvent
    ) = flow {

        val apiResult = safeApiCall(IO) {
            openApiMainService.updateUserloanrequest(
                "Token ${authToken.token!!}",
                loanrequest_pk = loanrequestPk,
                title = title,
                body = body,
                loanamount = loanamount

            )
        }
        emit(
            object : ApiResponseHandler<RootViewState, UserloanrequestCreateUpdateResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: UserloanrequestCreateUpdateResponse): DataState<RootViewState> {

                    val updatedUserloanrequest = resultObj.toUserloanrequestroom()

                    userloanrequestDao.updateUserloanrequest(
                        updatedUserloanrequest.pk,
                        updatedUserloanrequest.title,
                        updatedUserloanrequest.body,
                        updatedUserloanrequest.loanamount
                    )

                    return DataState.data(
                        response = Response(
                            message = resultObj.title,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = RootViewState(
                            viewUserloanrequestFields = ViewUserloanrequestFields(
                                userloanrequestRoom = updatedUserloanrequest
                            ),
                            updatedUserloanrequestFields = UpdatedUserloanrequestFields(
                                updatedUserloanrequestTitle = updatedUserloanrequest.title,
                                updatedUserloanrequestBody = updatedUserloanrequest.body,
                                updatedUserloanrequestLoanamount = updatedUserloanrequest.loanamount
                            )
                        ),
                        stateEvent = stateEvent
                    )

                }

            }.getResult()
        )
    }


    override fun searchUsersavingrequests(
        authToken: AuthToken,
        query: String,
        authorsender: String,
        filterAndOrder: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<RootViewState>> {
        return object :
            NetworkBoundResource<UsersavingrequestListSearchResponse, List<UsersavingrequestRoom>, RootViewState>(
                dispatcher =  IO,
                stateEvent = stateEvent,
                apiCall = {
                    openApiMainService.searchListUsersavingrequest(
                        authorsender = authorsender,
                        query = query,
                        ordering = filterAndOrder,
                        page = page
                    )
                },
                cacheCall = {
                    usersavingrequestDao.returnOrderedUsersavingrequestQuery(
                        query = query,
                        filterAndOrder = filterAndOrder,
                        page = page,
                        authorsender = authorsender
                    )
                }
            ) {
            override suspend fun updateCache(networkObject: UsersavingrequestListSearchResponse) {
                val usersavingrequestList = networkObject.toList()
                withContext( IO) {
                    for (usersavingrequest in usersavingrequestList) {
                        try {
                            // Launch each insert as a separate job to be executed in parallel
                            launch {
                                Log.d(TAG, "updateLocalDb: inserting usersavingrequest: ${usersavingrequest}")
                                usersavingrequestDao.insert(usersavingrequest)
                            }
                        } catch (e: Exception) {
                            Log.e(
                                TAG,
                                "updateLocalDb: error updating cache data on usersavingrequest post with slug: ${usersavingrequest.pk}. " +
                                        "${e.message}"
                            )
                            // Could send an error report here or something but I don't think you should throw an error to the UI
                            // Since there could be many usersavingrequest posts being inserted/updated.
                        }
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: List<UsersavingrequestRoom>): DataState<RootViewState> {

                Log.d(TAG, "handleCacheSuccess: 327: resultObjuserpostList ${resultObj}")

                val viewState = RootViewState(
                    usersavingrequestFields = RootViewState.UsersavingrequestFields(
                        usersavingrequestList = resultObj
                    )
                )
                return DataState.data(
                    response = null,
                    data = viewState,
                    stateEvent = stateEvent
                )
            }

        }.result
    }


    override fun deleteUsersavingrequest(
        authToken: AuthToken,
        usersavingrequestRoom: UsersavingrequestRoom,
        stateEvent: StateEvent
    ) = flow {
        val apiResult = safeApiCall( IO) {
            openApiMainService.deleteUsersavingrequest(
                "Token ${authToken.token!!}",
                usersavingrequestRoom.pk.toString()
            )
        }
        emit(
            object : ApiResponseHandler<RootViewState, GenericResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: GenericResponse): DataState<RootViewState> {

                    if (resultObj.response == SuccessHandling.SUCCESS_USERSAVINGREQUEST_DELETED) {
                        usersavingrequestDao.deleteUsersavingrequest(usersavingrequestRoom)
                        return DataState.data(
                            response = Response(
                                message = SuccessHandling.SUCCESS_USERSAVINGREQUEST_DELETED,
                                uiComponentType = UIComponentType.Toast(),
                                messageType = MessageType.Success()
                            ),
                            stateEvent = stateEvent
                        )
                    } else {
                        return buildError(
                            ErrorHandling.ERROR_UNKNOWN,
                            UIComponentType.Dialog(),
                            stateEvent
                        )
                    }
                }
            }.getResult()
        )
    }

    override fun updateUsersavingrequest(
        authToken: AuthToken,
        savingrequestPk: String,
        title: String,
        body: String,
        savingamount: Int,
        stateEvent: StateEvent
    ) = flow {

        val apiResult = safeApiCall( IO) {
            openApiMainService.updateUsersavingrequest(
                "Token ${authToken.token!!}",
                savingrequest_pk = savingrequestPk,
                title = title,
                body = body,
                savingamount = savingamount

            )
        }
        emit(
            object : ApiResponseHandler<RootViewState, UsersavingrequestCreateUpdateResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: UsersavingrequestCreateUpdateResponse): DataState<RootViewState> {

                    val updatedUsersavingrequest = resultObj.toUsersavingrequestroom()

                    usersavingrequestDao.updateUsersavingrequest(
                        updatedUsersavingrequest.pk,
                        updatedUsersavingrequest.title,
                        updatedUsersavingrequest.body,
                        updatedUsersavingrequest.savingamount
                    )

                    return DataState.data(
                        response = Response(
                            message = resultObj.title,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = RootViewState(
                            viewUsersavingrequestFields = RootViewState.ViewUsersavingrequestFields(
                                usersavingrequestRoom = updatedUsersavingrequest
                            ),
                            updatedUsersavingrequestFields = RootViewState.UpdatedUsersavingrequestFields(
                                updatedUsersavingrequestTitle = updatedUsersavingrequest.title,
                                updatedUsersavingrequestBody = updatedUsersavingrequest.body,
                                updatedUsersavingrequestSavingamount = updatedUsersavingrequest.savingamount

                            )
                        ),
                        stateEvent = stateEvent
                    )

                }

            }.getResult()
        )
    }

}
















