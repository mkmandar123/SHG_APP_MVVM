package com.digitaldetox.aww.ui.main.create_userloanrequest

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.digitaldetox.aww.di.main.MainScope
import com.digitaldetox.aww.repository.main.CreateUserloanrequestRepositoryImpl
import com.digitaldetox.aww.session.SessionManager
import com.digitaldetox.aww.ui.BaseViewModel
import com.digitaldetox.aww.ui.main.create_userloanrequest.state.CreateUserloanrequestStateEvent.CreateNewUserloanrequestEvent
import com.digitaldetox.aww.ui.main.create_userloanrequest.state.CreateUserloanrequestViewState
import com.digitaldetox.aww.ui.main.create_userloanrequest.state.CreateUserloanrequestViewState.NewUserloanrequestFields
import com.digitaldetox.aww.upload.ImageUploaderUserloanrequestCreate
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
class CreateUserloanrequestViewModel
@Inject
constructor(
    val createUserloanrequestRepository: CreateUserloanrequestRepositoryImpl,
    val sessionManager: SessionManager
) : BaseViewModel<CreateUserloanrequestViewState>() {

    private val _albumId: MutableLiveData<String> = MutableLiveData()

    fun load(id: String?) {

        _albumId.value = id
        Log.d(TAG, "load: is called. ${_albumId.value}")
    }

    override fun handleNewData(data: CreateUserloanrequestViewState) {

        setNewUserloanrequestFields(
            data.userloanrequestFields.newUserloanrequestTitle,
            data.userloanrequestFields.newUserloanrequestBody,
            data.userloanrequestFields.newUserloanrequestLoanamount
        )
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        sessionManager.cachedToken.value?.let { authToken ->
            val job: Flow<DataState<CreateUserloanrequestViewState>> = when (stateEvent) {

                is CreateNewUserloanrequestEvent -> {

                    createUserloanrequestRepository.createNewUserloanrequest(
                        stateEvent = stateEvent,
                        albumId =  _albumId.value!!,
                        title = stateEvent.title,
                        body = stateEvent.body,
                        loanamount = stateEvent.loanamount,
                        subreddit =  stateEvent.subreddit,
                        authorsender = stateEvent.authorsender
                    ) {
                        ImageUploaderUserloanrequestCreate.enqueue()
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

    override fun initNewViewState(): CreateUserloanrequestViewState {
        return CreateUserloanrequestViewState()
    }

    fun setNewUserloanrequestFields(title: String?, body: String?, loanamount: Int?) {
        val update = getCurrentViewStateOrNew()
        val newUserloanrequestFields = update.userloanrequestFields
        title?.let { newUserloanrequestFields.newUserloanrequestTitle = it }
        body?.let { newUserloanrequestFields.newUserloanrequestBody = it }
        loanamount?.let { newUserloanrequestFields.newUserloanrequestLoanamount = it }
        update.userloanrequestFields = newUserloanrequestFields
        setViewState(update)
    }

    fun clearNewUserloanrequestFields() {
        val update = getCurrentViewStateOrNew()
        update.userloanrequestFields = NewUserloanrequestFields()
        setViewState(update)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}





