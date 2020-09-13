package com.digitaldetox.aww.ui.main.root.viewmodel


import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.digitaldetox.aww.di.main.MainScope
import com.digitaldetox.aww.persistence.RootQueryUtils
import com.digitaldetox.aww.repository.main.RootRepositoryImpl
import com.digitaldetox.aww.session.SessionManager
import com.digitaldetox.aww.ui.BaseViewModel
import com.digitaldetox.aww.ui.main.root.state.RootStateEvent.*
import com.digitaldetox.aww.ui.main.root.state.RootViewState
import com.digitaldetox.aww.util.*
import com.digitaldetox.aww.util.ErrorHandling.Companion.INVALID_STATE_EVENT
import com.digitaldetox.aww.util.PreferenceKeys.Companion.HUMANLOANPROFILE_FILTER
import com.digitaldetox.aww.util.PreferenceKeys.Companion.HUMANLOANPROFILE_ORDER
import com.digitaldetox.aww.util.PreferenceKeys.Companion.HUMANSAVINGPROFILE_FILTER
import com.digitaldetox.aww.util.PreferenceKeys.Companion.HUMANSAVINGPROFILE_ORDER
import com.digitaldetox.aww.util.PreferenceKeys.Companion.SUBREDDIT_FILTER
import com.digitaldetox.aww.util.PreferenceKeys.Companion.SUBREDDIT_ORDER
import com.digitaldetox.aww.util.PreferenceKeys.Companion.SUBUSER_FILTER
import com.digitaldetox.aww.util.PreferenceKeys.Companion.SUBUSER_ORDER
import com.digitaldetox.aww.util.PreferenceKeys.Companion.USERLOANREQUEST_FILTER
import com.digitaldetox.aww.util.PreferenceKeys.Companion.USERLOANREQUEST_ORDER
import handleIncomingUserloanrequestListData
import com.digitaldetox.aww.util.PreferenceKeys.Companion.USERSAVINGREQUEST_FILTER
import com.digitaldetox.aww.util.PreferenceKeys.Companion.USERSAVINGREQUEST_ORDER
import handleIncomingHumanloanprofileListData
import handleIncomingHumansavingprofileListData
import handleIncomingUsersavingrequestListData
import handleIncomingSubredditListData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@MainScope
class RootViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val rootRepository: RootRepositoryImpl,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
) : BaseViewModel<RootViewState>() {

    private val _albumId: MutableLiveData<String> = MutableLiveData()
    fun loadRoot(id: String?) {

        _albumId.value = id
        Log.d(TAG, "load: is called. ${_albumId.value}")
    }

    init {

        setHumansavingprofileFilterHumansavingprofile(
            sharedPreferences.getString(
                HUMANSAVINGPROFILE_FILTER,
                RootQueryUtils.HUMANSAVINGPROFILE_FILTER_DATE_UPDATED
            )
        )
        setHumansavingprofileOrderHumansavingprofile(
            sharedPreferences.getString(
                HUMANSAVINGPROFILE_ORDER,
                RootQueryUtils.HUMANSAVINGPROFILE_ORDER_DESC
            )
        )

        setHumanloanprofileFilterHumanloanprofile(
            sharedPreferences.getString(
                HUMANLOANPROFILE_FILTER,
                RootQueryUtils.HUMANLOANPROFILE_FILTER_DATE_UPDATED
            )
        )
        setHumanloanprofileOrderHumanloanprofile(
            sharedPreferences.getString(
                HUMANLOANPROFILE_ORDER,
                RootQueryUtils.HUMANLOANPROFILE_ORDER_DESC
            )
        )

        setSubredditFilterSubreddit(
            sharedPreferences.getString(
                SUBREDDIT_FILTER,
                RootQueryUtils.SUBREDDIT_FILTER_DATE_UPDATED
            )
        )
        setSubredditOrderSubreddit(
            sharedPreferences.getString(
                SUBREDDIT_ORDER,
                RootQueryUtils.SUBREDDIT_ORDER_DESC
            )
        )

        setSubuserFilterSubuser(
            sharedPreferences.getString(
                SUBUSER_FILTER,
                RootQueryUtils.SUBUSER_FILTER_DATE_UPDATED
            )
        )
        setSubuserOrderSubuser(
            sharedPreferences.getString(
                SUBUSER_ORDER,
                RootQueryUtils.SUBUSER_ORDER_DESC
            )
        )

        setUserloanrequestFilterUserloanrequest(
            sharedPreferences.getString(
                USERLOANREQUEST_FILTER,
                RootQueryUtils.USERLOANREQUEST_FILTER_DATE_UPDATED
            )
        )
        setUserloanrequestOrderUserloanrequest(
            sharedPreferences.getString(
                USERLOANREQUEST_ORDER,
                RootQueryUtils.USERLOANREQUEST_ORDER_DESC
            )
        )


        setUsersavingrequestFilterUsersavingrequest(
            sharedPreferences.getString(
                USERSAVINGREQUEST_FILTER,
                RootQueryUtils.USERSAVINGREQUEST_FILTER_DATE_UPDATED
            )
        )
        setUsersavingrequestOrderUsersavingrequest(
            sharedPreferences.getString(
                USERSAVINGREQUEST_ORDER,
                RootQueryUtils.USERSAVINGREQUEST_ORDER_DESC
            )
        )
    }

    override fun handleNewData(data: RootViewState) {

        data.humansavingprofileFields.let { humansavingprofileFields ->

            humansavingprofileFields.humansavingprofileList?.let { humansavingprofileList ->
                handleIncomingHumansavingprofileListData(data)
            }

            humansavingprofileFields.isQueryExhausted?.let { isQueryExhausted ->
                setQueryExhaustedHumansavingprofile(isQueryExhausted)
            }

        }


        data.humanloanprofileFields.let { humanloanprofileFields ->

            humanloanprofileFields.humanloanprofileList?.let { humanloanprofileList ->
                handleIncomingHumanloanprofileListData(data)
            }

            humanloanprofileFields.isQueryExhausted?.let { isQueryExhausted ->
                setQueryExhaustedHumanloanprofile(isQueryExhausted)
            }

        }

        data.subredditFields.let { subredditFields ->

            subredditFields.subredditList?.let { subredditList ->
                handleIncomingSubredditListData(data)
            }

            subredditFields.isQueryExhausted?.let { isQueryExhausted ->
                setQueryExhaustedSubreddit(isQueryExhausted)
            }

        }

        data.viewSubredditFields.let { viewSubredditFields ->

            viewSubredditFields.subredditRoom?.let { subreddit ->
                setSubreddit(subreddit)
            }

            viewSubredditFields.isAuthorOfSubreddit?.let { isAuthor ->
                setIsAuthorOfSubreddit(isAuthor)
            }
        }

        data.updatedSubredditFields.let { updatedSubredditFields ->

            updatedSubredditFields.updatedSubredditTitle?.let { title ->
                setUpdatedTitleSubreddit(title)
            }

            updatedSubredditFields.updatedSubredditBody?.let { body ->
                setUpdatedBodySubreddit(body)
            }
        }

        data.userloanrequestFields.let { userloanrequestFields ->

            userloanrequestFields.userloanrequestList?.let { userloanrequestList ->
                handleIncomingUserloanrequestListData(data)
            }

            userloanrequestFields.isQueryExhausted?.let { isQueryExhausted ->
                setQueryExhaustedUserloanrequest(isQueryExhausted)
            }

        }

        data.viewUserloanrequestFields.let { viewUserloanrequestFields ->

            viewUserloanrequestFields.userloanrequestRoom?.let { userloanrequest ->
                setUserloanrequest(userloanrequest)
            }

            viewUserloanrequestFields.isAuthorOfUserloanrequest?.let { isAuthor ->
                setIsAuthorOfUserloanrequest(isAuthor)
            }
        }

        data.updatedUserloanrequestFields.let { updatedUserloanrequestFields ->

            updatedUserloanrequestFields.updatedUserloanrequestTitle?.let { title ->
                setUpdatedTitleUserloanrequest(title)
            }

            updatedUserloanrequestFields.updatedUserloanrequestBody?.let { body ->
                setUpdatedBodyUserloanrequest(body)
            }

            updatedUserloanrequestFields.updatedUserloanrequestLoanamount?.let { loanamount ->
                setUpdatedLoanamountUserloanrequest(loanamount)
            }
        }


        data.usersavingrequestFields.let { usersavingrequestFields ->

            usersavingrequestFields.usersavingrequestList?.let { usersavingrequestList ->
                handleIncomingUsersavingrequestListData(data)
            }

            usersavingrequestFields.isQueryExhausted?.let { isQueryExhausted ->
                setQueryExhaustedUsersavingrequest(isQueryExhausted)
            }

        }

        data.viewUsersavingrequestFields.let { viewUsersavingrequestFields ->

            viewUsersavingrequestFields.usersavingrequestRoom?.let { usersavingrequest ->
                setUsersavingrequest(usersavingrequest)
            }

            viewUsersavingrequestFields.isAuthorOfUsersavingrequest?.let { isAuthor ->
                setIsAuthorOfUsersavingrequest(isAuthor)
            }
        }

        data.updatedUsersavingrequestFields.let { updatedUsersavingrequestFields ->

            updatedUsersavingrequestFields.updatedUsersavingrequestTitle?.let { title ->
                setUpdatedTitleUsersavingrequest(title)
            }

            updatedUsersavingrequestFields.updatedUsersavingrequestBody?.let { body ->
                setUpdatedBodyUsersavingrequest(body)
            }

            updatedUsersavingrequestFields.updatedUsersavingrequestSavingamount?.let { savingamount ->
                setUpdatedSavingamountUsersavingrequest(savingamount)
            }
        }



        data.subuserFields.let { subuserFields ->

            //            subuserFields.subuserList?.let { subuserList ->
//                handleIncomingSubuserListData(data)
//            }

            subuserFields.isQueryExhausted?.let { isQueryExhausted ->
                setQueryExhaustedSubuser(isQueryExhausted)
            }

        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        if (!isJobAlreadyActive(stateEvent)) {
            sessionManager.cachedToken.value?.let { authToken ->
                val job: Flow<DataState<RootViewState>> = when (stateEvent) {

                    is HumansavingprofileSearchEvent -> {
                        if (stateEvent.clearLayoutManagerState) {
                            clearLayoutManagerStateHumansavingprofile()
                        }
                        rootRepository.searchHumansavingprofiles(
                            stateEvent = stateEvent,
                            username = sharedPreferences.getString(
                                PreferenceKeys.PREVIOUS_AUTH_USER,
                                null
                            )
                        )
                    }



                    is HumanloanprofileSearchEvent -> {
                        if (stateEvent.clearLayoutManagerState) {
                            clearLayoutManagerStateHumanloanprofile()
                        }
                        rootRepository.searchHumanloanprofiles(
                            stateEvent = stateEvent,
                            username = sharedPreferences.getString(
                                PreferenceKeys.PREVIOUS_AUTH_USER,
                                null
                            )
                        )
                    }


                    is SubredditSearchEvent -> {
                        if (stateEvent.clearLayoutManagerState) {
                            clearLayoutManagerStateSubreddit()
                        }
                        rootRepository.searchSubreddits(
                            stateEvent = stateEvent,
                            authToken = authToken,
                            username = sharedPreferences.getString(
                                PreferenceKeys.PREVIOUS_AUTH_USER,
                                null
                            ),
                            query = getSearchQuerySubreddit(),
                            filterAndOrder = getOrderSubreddit() + getFilterSubreddit(),
                            page = getPageSubreddit()
                        )
                    }


                    is DeleteSubredditPostEvent -> {
                        rootRepository.deleteSubreddit(
                            stateEvent = stateEvent,
                            authToken = authToken,
                            subredditRoom = getSubreddit()
                        )
                    }

                    is UpdateSubredditPostEvent -> {

                        rootRepository.updateSubreddit(
                            stateEvent = stateEvent,
                            authToken = authToken,
                            subreddit = getSubreddit().title,
                            title = stateEvent.title,
                            description = stateEvent.body
                        )
                    }

                    is UserloanrequestSearchEvent -> {
                        if (stateEvent.clearLayoutManagerState) {
                            clearLayoutManagerStateUserloanrequest()
                        }
                        rootRepository.searchUserloanrequests(
                            stateEvent = stateEvent,
                            authToken = authToken,
                            query = getSearchQueryUserloanrequest(),
                            authorsender = getAuthorsenderNamePassSubuser(),
                            filterAndOrder = getOrderUserloanrequest() + getFilterUserloanrequest(),
                            page = getPageUserloanrequest()
                        )
                    }


                    is DeleteUserloanrequestPostEvent -> {
                        rootRepository.deleteUserloanrequest(
                            stateEvent = stateEvent,
                            authToken = authToken,
                            userloanrequestRoom = getUserloanrequest()
                        )
                    }

                    is UpdateUserloanrequestPostEvent -> {

                        rootRepository.updateUserloanrequest(
                            stateEvent = stateEvent,
                            authToken = authToken,
                            loanrequestPk = getUserloanrequestPk(),
                            title = stateEvent.title,
                            body = stateEvent.body,
                            loanamount = stateEvent.loanamount
                        )
                    }

                    is UsersavingrequestSearchEvent -> {
                        if (stateEvent.clearLayoutManagerState) {
                            clearLayoutManagerStateUsersavingrequest()
                        }
                        rootRepository.searchUsersavingrequests(
                            stateEvent = stateEvent,
                            authToken = authToken,
                            query = getSearchQueryUsersavingrequest(),
                            authorsender = getAuthorsenderNamePassSubuser(),
                            filterAndOrder = getOrderUsersavingrequest() + getFilterUsersavingrequest(),
                            page = getPageUsersavingrequest()
                        )
                    }


                    is DeleteUsersavingrequestPostEvent -> {
                        rootRepository.deleteUsersavingrequest(
                            stateEvent = stateEvent,
                            authToken = authToken,
                            usersavingrequestRoom = getUsersavingrequest()
                        )
                    }

                    is UpdateUsersavingrequestPostEvent -> {

                        rootRepository.updateUsersavingrequest(
                            stateEvent = stateEvent,
                            authToken = authToken,
                            savingrequestPk = getUsersavingrequestPk(),
                            title = stateEvent.title,
                            body = stateEvent.body,
                            savingamount = stateEvent.savingamount
                        )
                    }



                    else -> {
                        flow {
                            emit(
                                DataState.error(
                                    response = Response(
                                        message = INVALID_STATE_EVENT,
                                        uiComponentType = UIComponentType.None(),
                                        messageType = MessageType.Error()
                                    ),
                                    stateEvent = stateEvent
                                )
                            )
                        }
                    }
                }
                launchJob(stateEvent, job)
            }
        }
    }

    override fun initNewViewState(): RootViewState {
        return RootViewState()
    }

    fun saveFilterOptionsSubreddit(filter: String, order: String) {
        editor.putString(SUBREDDIT_FILTER, filter)
        editor.apply()

        editor.putString(SUBREDDIT_ORDER, order)
        editor.apply()
    }


    fun saveFilterOptionsSubuser(filter: String, order: String) {
        editor.putString(SUBUSER_FILTER, filter)
        editor.apply()

        editor.putString(SUBUSER_ORDER, order)
        editor.apply()
    }

    fun saveFilterOptionsUserloanrequest(filter: String, order: String) {
        editor.putString(USERLOANREQUEST_FILTER, filter)
        editor.apply()

        editor.putString(USERLOANREQUEST_ORDER, order)
        editor.apply()
    }


    fun saveFilterOptionsUsersavingrequest(filter: String, order: String) {
        editor.putString(PreferenceKeys.USERSAVINGREQUEST_FILTER, filter)
        editor.apply()

        editor.putString(PreferenceKeys.USERSAVINGREQUEST_ORDER, order)
        editor.apply()
    }


    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}
















