import android.util.Log
import com.digitaldetox.aww.ui.main.root.state.RootStateEvent
import com.digitaldetox.aww.ui.main.root.state.RootStateEvent.*
import com.digitaldetox.aww.ui.main.root.state.RootViewState
import com.digitaldetox.aww.ui.main.root.viewmodel.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.resetPageSubreddit(){
    val update = getCurrentViewStateOrNew()
    update.subredditFields.page = 1
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.refreshFromCacheSubreddit(){
    if(!isJobAlreadyActive(SubredditSearchEvent())){
        setQueryExhaustedSubreddit(false)
        setStateEvent(SubredditSearchEvent(false))
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.refreshFromCacheHumanloanprofile(){
    if(!isJobAlreadyActive(RootStateEvent.HumanloanprofileSearchEvent())){
        setQueryExhaustedHumanloanprofile(false)
        setStateEvent(RootStateEvent.HumanloanprofileSearchEvent(false))
    }
}
@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.handleIncomingHumanloanprofileListData(viewState: RootViewState){
    viewState.humanloanprofileFields.let { humanloanprofileFields ->
        humanloanprofileFields.humanloanprofileList?.let { setHumanloanprofileListData(it) }
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.refreshFromCacheHumansavingprofile(){
    if(!isJobAlreadyActive(RootStateEvent.HumansavingprofileSearchEvent())){
        setQueryExhaustedHumansavingprofile(false)
        setStateEvent(RootStateEvent.HumansavingprofileSearchEvent(false))
    }
}
@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.handleIncomingHumansavingprofileListData(viewState: RootViewState){
    viewState.humansavingprofileFields.let { humansavingprofileFields ->
        humansavingprofileFields.humansavingprofileList?.let { setHumansavingprofileListData(it) }
    }
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.loadFirstPageSubreddit() {
    if(!isJobAlreadyActive(SubredditSearchEvent())){
        setQueryExhaustedSubreddit(false)
        resetPageSubreddit()
        setStateEvent(SubredditSearchEvent())
        Log.e(TAG, "RootViewModel: loadFirstPageSubreddit: ${viewState.value!!.subredditFields.searchQuery}")
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
private fun RootViewModel.incrementPageNumberSubreddit(){
    val update = getCurrentViewStateOrNew()
    val page = update.copy().subredditFields.page ?: 1
    update.subredditFields.page = page.plus(1)
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.nextPageSubreddit(){
    if(!isJobAlreadyActive(SubredditSearchEvent())
        && !viewState.value!!.subredditFields.isQueryExhausted!!
    ){
        Log.d(TAG, "RootViewModel: Attempting to load next page...")
        incrementPageNumberSubreddit()
        setStateEvent(SubredditSearchEvent())
    }
}



@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.handleIncomingSubredditListData(viewState: RootViewState){
    viewState.subredditFields.let { subredditFields ->
        subredditFields.subredditList?.let { setSubredditListData(it) }
    }
}



@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.resetPageSubuser(){
    val update = getCurrentViewStateOrNew()
    update.subuserFields.page = 1
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.refreshFromCacheSubuser(){
    if(!isJobAlreadyActive(SubuserSearchEvent())){
        setQueryExhaustedSubuser(false)
        setStateEvent(SubuserSearchEvent(false))
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.loadFirstPageSubuser() {
    if(!isJobAlreadyActive(SubuserSearchEvent())){
        setQueryExhaustedSubuser(false)
        resetPageSubuser()
        setStateEvent(SubuserSearchEvent())
        Log.e(TAG, "RootViewModel: loadFirstPageSubuser: ${viewState.value!!.subuserFields.searchQuery}")
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
private fun RootViewModel.incrementPageNumberSubuser(){
    val update = getCurrentViewStateOrNew()
    val page = update.copy().subuserFields.page ?: 1
    update.subuserFields.page = page.plus(1)
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.nextPageSubuser(){
    if(!isJobAlreadyActive(SubuserSearchEvent())
        && !viewState.value!!.subuserFields.isQueryExhausted!!
    ){
        Log.d(TAG, "RootViewModel: Attempting to load next page...")
        incrementPageNumberSubuser()
        setStateEvent(SubuserSearchEvent())
    }
}

/* USERLOANREQUEST */


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.resetPageUserloanrequest(){
    val update = getCurrentViewStateOrNew()
    update.userloanrequestFields.page = 1
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.refreshFromCacheUserloanrequest(){
    if(!isJobAlreadyActive(UserloanrequestSearchEvent())){
        setQueryExhaustedUserloanrequest(false)
        setStateEvent(UserloanrequestSearchEvent(false))
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.loadFirstPageUserloanrequest() {
    if(!isJobAlreadyActive(UserloanrequestSearchEvent())){
        setQueryExhaustedUserloanrequest(false)
        resetPageUserloanrequest()
        setStateEvent(UserloanrequestSearchEvent())
        Log.e(TAG, "RootViewModel: loadFirstPageUserloanrequest: ${viewState.value!!.userloanrequestFields.searchQuery}")
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
private fun RootViewModel.incrementPageNumberUserloanrequest(){
    val update = getCurrentViewStateOrNew()
    val page = update.copy().userloanrequestFields.page ?: 1
    update.userloanrequestFields.page = page.plus(1)
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.nextPageUserloanrequest(){
    if(!isJobAlreadyActive(UserloanrequestSearchEvent())
        && !viewState.value!!.userloanrequestFields.isQueryExhausted!!
    ){
        Log.d(TAG, "RootViewModel: Attempting to load next page...")
        incrementPageNumberUserloanrequest()
        setStateEvent(UserloanrequestSearchEvent())
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.handleIncomingUserloanrequestListData(viewState: RootViewState){
    viewState.userloanrequestFields.let { userloanrequestFields ->
        userloanrequestFields.userloanrequestList?.let { setUserloanrequestListData(it) }
    }
}



/* end USERLOANREQUEST */



/* USERSAVINGREQUEST */


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.resetPageUsersavingrequest(){
    val update = getCurrentViewStateOrNew()
    update.usersavingrequestFields.page = 1
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.refreshFromCacheUsersavingrequest(){
    if(!isJobAlreadyActive(RootStateEvent.UsersavingrequestSearchEvent())){
        setQueryExhaustedUsersavingrequest(false)
        setStateEvent(RootStateEvent.UsersavingrequestSearchEvent(false))
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.loadFirstPageUsersavingrequest() {
    if(!isJobAlreadyActive(RootStateEvent.UsersavingrequestSearchEvent())){
        setQueryExhaustedUsersavingrequest(false)
        resetPageUsersavingrequest()
        setStateEvent(RootStateEvent.UsersavingrequestSearchEvent())
        Log.e(TAG, "RootViewModel: loadFirstPageUsersavingrequest: ${viewState.value!!.usersavingrequestFields.searchQuery}")
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
private fun RootViewModel.incrementPageNumberUsersavingrequest(){
    val update = getCurrentViewStateOrNew()
    val page = update.copy().usersavingrequestFields.page ?: 1
    update.usersavingrequestFields.page = page.plus(1)
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.nextPageUsersavingrequest(){
    if(!isJobAlreadyActive(RootStateEvent.UsersavingrequestSearchEvent())
        && !viewState.value!!.usersavingrequestFields.isQueryExhausted!!
    ){
        Log.d(TAG, "RootViewModel: Attempting to load next page...")
        incrementPageNumberUsersavingrequest()
        setStateEvent(RootStateEvent.UsersavingrequestSearchEvent())
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RootViewModel.handleIncomingUsersavingrequestListData(viewState: RootViewState){
    viewState.usersavingrequestFields.let { usersavingrequestFields ->
        usersavingrequestFields.usersavingrequestList?.let { setUsersavingrequestListData(it) }
    }
}



/* end USERSAVINGREQUEST */

