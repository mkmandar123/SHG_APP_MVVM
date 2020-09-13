package com.digitaldetox.aww.ui.main.account

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.digitaldetox.aww.R
import com.digitaldetox.aww.models.AccountProperties
import com.digitaldetox.aww.ui.main.account.state.ACCOUNT_VIEW_STATE_BUNDLE_KEY
import com.digitaldetox.aww.ui.main.account.state.AccountStateEvent.*
import com.digitaldetox.aww.ui.main.account.state.AccountViewState
import com.digitaldetox.aww.util.StateMessageCallback
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class AccountFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory
): BaseAccountFragment(R.layout.fragment_account, viewModelFactory) {

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
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.profile)
//        change_password.setOnClickListener{
//            findNavController().navigate(R.id.action_accountFragment_to_changePasswordFragment)
//        }

        logout_button.setOnClickListener {
            viewModel.logout()

            viewModel.cleanUpDeleteAfterLogout()
        }

        subscribeObservers()
    }

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer{ viewState->
            if(viewState != null){
                viewState.accountProperties?.let{
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
                    stateMessageCallback = object: StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(GetAccountPropertiesEvent())
    }

    private fun setAccountDataFields(accountProperties: AccountProperties){

        val mPrefs: SharedPreferences = activity!!.getSharedPreferences("accountkey", Context.MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
//        Toast.makeText(context, "pref: ${accountProperties.email}", Toast.LENGTH_SHORT).show()
        prefsEditor.putString("accountEmail", "${accountProperties.email}")
        prefsEditor.putString("accountUsername", "${accountProperties.username}")
        prefsEditor.putString("accountSavingtarget", "${accountProperties.savingtarget}")
        prefsEditor.apply()

        email?.text = accountProperties.email
        username?.text = accountProperties.username
        firstname?.text = accountProperties.first_name
        lastname?.text = accountProperties.last_name
        location?.text = accountProperties.location
        age?.text = accountProperties.age.toString()
        savingtarget?.text = accountProperties.savingtarget.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_view_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit -> {
                findNavController().navigate(R.id.action_accountFragment_to_updateAccountFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}