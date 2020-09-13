package com.digitaldetox.aww.ui.main.create_subreddit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.digitaldetox.aww.di.main.MainScope
import com.digitaldetox.aww.repository.main.CreateSubredditRepositoryImpl
import com.digitaldetox.aww.session.SessionManager
import com.digitaldetox.aww.ui.BaseViewModel
import com.digitaldetox.aww.ui.main.create_subreddit.state.CreateSubredditStateEvent.CreateNewSubredditEvent
import com.digitaldetox.aww.ui.main.create_subreddit.state.CreateSubredditViewState
import com.digitaldetox.aww.ui.main.create_subreddit.state.CreateSubredditViewState.NewSubredditFields
import com.digitaldetox.aww.upload.ImageUploaderSubredditCreate
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
class CreateSubredditViewModel
@Inject
constructor(
    val createSubredditRepository: CreateSubredditRepositoryImpl,
    val sessionManager: SessionManager
) : BaseViewModel<CreateSubredditViewState>() {

    private val _albumId: MutableLiveData<String> = MutableLiveData()

    fun load(id: String?) {

        _albumId.value = id
        Log.d(TAG, "load: is called. ${_albumId.value}")
    }

    override fun handleNewData(data: CreateSubredditViewState) {

        setNewSubredditFields(
            data.subredditFields.newSubredditTitle,
            data.subredditFields.newSubredditBody
        )
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        sessionManager.cachedToken.value?.let { authToken ->
            val job: Flow<DataState<CreateSubredditViewState>> = when (stateEvent) {

                is CreateNewSubredditEvent -> {

                    createSubredditRepository.createNewSubreddit(
                        stateEvent = stateEvent,
                        albumId = _albumId.value!!,
                        title = stateEvent.title,
                        body = stateEvent.body
                    ) {
                        ImageUploaderSubredditCreate.enqueue()
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

    override fun initNewViewState(): CreateSubredditViewState {
        return CreateSubredditViewState()
    }

    fun setNewSubredditFields(title: String?, body: String?) {
        val update = getCurrentViewStateOrNew()
        val newSubredditFields = update.subredditFields
        title?.let { newSubredditFields.newSubredditTitle = it }
        body?.let { newSubredditFields.newSubredditBody = it }
        update.subredditFields = newSubredditFields
        setViewState(update)
    }

    fun clearNewSubredditFields() {
        val update = getCurrentViewStateOrNew()
        update.subredditFields = NewSubredditFields()
        setViewState(update)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}





