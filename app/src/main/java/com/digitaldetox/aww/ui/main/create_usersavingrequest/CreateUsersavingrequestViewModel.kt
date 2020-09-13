package com.digitaldetox.aww.ui.main.create_usersavingrequest

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.digitaldetox.aww.di.main.MainScope
import com.digitaldetox.aww.repository.main.CreateUsersavingrequestRepositoryImpl
import com.digitaldetox.aww.session.SessionManager
import com.digitaldetox.aww.ui.BaseViewModel
import com.digitaldetox.aww.ui.main.create_usersavingrequest.state.CreateUsersavingrequestStateEvent.CreateNewUsersavingrequestEvent
import com.digitaldetox.aww.ui.main.create_usersavingrequest.state.CreateUsersavingrequestViewState
import com.digitaldetox.aww.ui.main.create_usersavingrequest.state.CreateUsersavingrequestViewState.NewUsersavingrequestFields
import com.digitaldetox.aww.upload.ImageUploaderUsersavingrequestCreate
import com.digitaldetox.aww.util.*
import com.digitaldetox.aww.util.ErrorHandling.Companion.INVALID_STATE_EVENT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseExperimental(ExperimentalCoroutinesApi::class)
@FlowPreview
@MainScope
class CreateUsersavingrequestViewModel
@Inject
constructor(
    val createUsersavingrequestRepository: CreateUsersavingrequestRepositoryImpl,
    val sessionManager: SessionManager
) : BaseViewModel<CreateUsersavingrequestViewState>() {

    private val _albumId: MutableLiveData<String> = MutableLiveData()

    fun load(id: String?) {

        _albumId.value = id
        Log.d(TAG, "load: is called. ${_albumId.value}")
    }

    override fun handleNewData(data: CreateUsersavingrequestViewState) {

        setNewUsersavingrequestFields(
            data.usersavingrequestFields.newUsersavingrequestTitle,
            data.usersavingrequestFields.newUsersavingrequestBody,
            data.usersavingrequestFields.newUsersavingrequestSavingamount
        )
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        sessionManager.cachedToken.value?.let { authToken ->
            val job: Flow<DataState<CreateUsersavingrequestViewState>> = when (stateEvent) {

                is CreateNewUsersavingrequestEvent -> {

                    createUsersavingrequestRepository.createNewUsersavingrequest(
                        stateEvent = stateEvent,
                        albumId =  _albumId.value!!,
                        title = stateEvent.title,
                        body = stateEvent.body,
                        savingamount = stateEvent.savingamount,
                        subreddit =  stateEvent.subreddit,
                        authorsender = stateEvent.authorsender
                    ) {
                        ImageUploaderUsersavingrequestCreate.enqueue()
                    }
                    return
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

    override fun initNewViewState(): CreateUsersavingrequestViewState {
        return CreateUsersavingrequestViewState()
    }

    fun setNewUsersavingrequestFields(title: String?, body: String?, savingamount: Int?) {
        val update = getCurrentViewStateOrNew()
        val newUsersavingrequestFields = update.usersavingrequestFields
        title?.let { newUsersavingrequestFields.newUsersavingrequestTitle = it }
        body?.let { newUsersavingrequestFields.newUsersavingrequestBody = it }
        savingamount?.let { newUsersavingrequestFields.newUsersavingrequestSavingamount = it }
        update.usersavingrequestFields = newUsersavingrequestFields
        setViewState(update)
    }

    fun clearNewUsersavingrequestFields() {
        val update = getCurrentViewStateOrNew()
        update.usersavingrequestFields = NewUsersavingrequestFields()
        setViewState(update)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}





