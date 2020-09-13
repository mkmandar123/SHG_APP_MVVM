package com.digitaldetox.aww.ui.main.root

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.digitaldetox.aww.R
import com.digitaldetox.aww.models.UserloanrequestRoom
import com.digitaldetox.aww.ui.AreYouSureCallback
import com.digitaldetox.aww.ui.main.root.state.ROOT_VIEW_STATE_BUNDLE_KEY
import com.digitaldetox.aww.ui.main.root.state.RootStateEvent.DeleteUserloanrequestPostEvent
import com.digitaldetox.aww.ui.main.root.state.RootViewState
import com.digitaldetox.aww.ui.main.root.viewmodel.*
import com.digitaldetox.aww.util.MessageType
import com.digitaldetox.aww.util.Response
import com.digitaldetox.aww.util.StateMessageCallback
import com.digitaldetox.aww.util.SuccessHandling.Companion.SUCCESS_USERLOANREQUEST_DELETED
import com.digitaldetox.aww.util.UIComponentType
import kotlinx.android.synthetic.main.fragment_view_userloanrequest.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class ViewUserloanrequestFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: RequestManager
) : BaseRootFragment(R.layout.fragment_view_userloanrequest, viewModelFactory) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let { inState ->
            (inState[ROOT_VIEW_STATE_BUNDLE_KEY] as RootViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value


        viewState?.userloanrequestFields?.userloanrequestList = ArrayList()

        outState.putParcelable(
            ROOT_VIEW_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.request_details)
        setHasOptionsMenu(true)
        subscribeObservers()
        uiCommunicationListener.expandAppBar()

        delete_button.setOnClickListener {
            confirmDeleteRequest()
        }

    }

    fun confirmDeleteRequest() {
        val callback: AreYouSureCallback = object : AreYouSureCallback {

            override fun proceed() {
                deleteUserloanrequest()
            }

            override fun cancel() {

            }
        }
        uiCommunicationListener.onResponseReceived(
            response = Response(
                message = getString(R.string.are_you_sure_delete),
                uiComponentType = UIComponentType.AreYouSureDialog(callback),
                messageType = MessageType.Info()
            ),
            stateMessageCallback = object : StateMessageCallback {
                override fun removeMessageFromStack() {
                    viewModel.clearStateMessage()
                }
            }
        )
    }

    fun deleteUserloanrequest() {
        viewModel.setStateEvent(
            DeleteUserloanrequestPostEvent()
        )
    }

//    fun checkIsAuthorOfUserloanrequest() {
//        viewModel.setIsAuthorOfUserloanrequest(false)
//    }

    fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.viewUserloanrequestFields.userloanrequestRoom?.let { userloanrequest ->
                setUserloanrequestProperties(userloanrequest)
            }

            if (viewState.viewUserloanrequestFields.isAuthorOfUserloanrequest == true) {
                adaptViewToAuthorMode()
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            if (stateMessage?.response?.message.equals(SUCCESS_USERLOANREQUEST_DELETED)) {
                viewModel.removeDeletedUserloanrequest()
                findNavController().popBackStack()
            }

            stateMessage?.let {
                uiCommunicationListener.onResponseReceived(
                    response = it.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })
    }

    fun adaptViewToAuthorMode() {
        activity?.invalidateOptionsMenu()
        delete_button.visibility = View.VISIBLE
    }

    fun setUserloanrequestProperties(userloanrequestRoom: UserloanrequestRoom) {
        userloanrequest_title.setText(userloanrequestRoom.title)
        userloanrequest_update_date.setText(userloanrequestRoom.albumId)
        userloanrequest_body.setText(userloanrequestRoom.body)
        userloanrequest_loanamount.setText(userloanrequestRoom.loanamount.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (viewModel.isAuthorOfUserloanrequest()) {
            inflater.inflate(R.menu.edit_view_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (viewModel.isAuthorOfUserloanrequest()) {
            when (item.itemId) {
                R.id.edit -> {
                    navUpdateUserloanrequestFragment()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navUpdateUserloanrequestFragment() {
        try {

            viewModel.setUpdatedTitleUserloanrequest(viewModel.getUserloanrequest().title)
            viewModel.setUpdatedBodyUserloanrequest(viewModel.getUserloanrequest().body)
            viewModel.setUpdatedLoanamountUserloanrequest(viewModel.getUserloanrequest().loanamount.toString().toInt())
            findNavController().navigate(R.id.action_viewUserloanrequestFragment_to_updateUserloanrequestFragment)
        } catch (e: Exception) {

            Log.e(TAG, "Exception: ${e.message}")
        }
    }
}

