package com.digitaldetox.aww.ui.main.account

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.digitaldetox.aww.R
import com.digitaldetox.aww.models.AccountProperties
import com.digitaldetox.aww.ui.main.account.state.ACCOUNT_VIEW_STATE_BUNDLE_KEY
import com.digitaldetox.aww.ui.main.account.state.AccountStateEvent
import com.digitaldetox.aww.ui.main.account.state.AccountViewState
import com.digitaldetox.aww.util.StateMessageCallback
import kotlinx.android.synthetic.main.fragment_update_account.*

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class UpdateAccountFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory
) : BaseAccountFragment(R.layout.fragment_update_account, viewModelFactory) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let { inState ->
            (inState[ACCOUNT_VIEW_STATE_BUNDLE_KEY] as AccountViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.update_prfofile)

        setHasOptionsMenu(true)
        subscribeObservers()
    }

    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {
                viewState.accountProperties?.let {
                    setAccountDataFields(it)
                }
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

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

    private fun setAccountDataFields(accountProperties: AccountProperties) {
        if (input_email.text.isNullOrBlank()) {
            input_email.setText(accountProperties.email)
        }
        if (input_username.text.isNullOrBlank()) {
            input_username.setText(accountProperties.username)
        }

        if (input_firstname.text.isNullOrBlank()) {
            input_firstname.setText(accountProperties.first_name)
        }

        if (input_lastname.text.isNullOrBlank()) {
            input_lastname.setText(accountProperties.last_name)
        }

        if (input_location.text.isNullOrBlank()) {
            input_location.setText(accountProperties.location)
        }


        if (input_age.text.isNullOrBlank()) {
            input_age.setText(accountProperties.age.toString())
        }

        if (input_savingtarget.text.isNullOrBlank()) {
            input_savingtarget.setText(accountProperties.savingtarget.toString())
        }
    }

    private fun saveChanges() {
        viewModel.setStateEvent(
            AccountStateEvent.UpdateAccountPropertiesEvent(
                input_email.text.toString(),
                input_username.text.toString(),
                input_firstname.text.toString(),
                input_lastname.text.toString(),
                input_location.text.toString(),
                input_age.text.toString().toInt(),
                input_savingtarget.text.toString().toInt()

            )
        )
        uiCommunicationListener.hideSoftKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                saveChanges()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}






