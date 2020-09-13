package com.digitaldetox.aww.messagingmvvm.fragments.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.digitaldetox.aww.R
import com.digitaldetox.aww.messagingmvvm.util.AuthUtil
import com.digitaldetox.aww.messagingmvvm.util.ErrorMessage
import com.digitaldetox.aww.messagingmvvm.util.LoadState
import com.digitaldetox.aww.messagingmvvm.util.eventbus_events.KeyboardEvent
import com.digitaldetox.aww.ui.UICommunicationListener
import com.digitaldetox.aww.ui.auth_messaging.BaseAuthMessagingFragment
import com.digitaldetox.aww.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login_messaging.*
import kotlinx.android.synthetic.main.issue_layout.view.*
import org.greenrobot.eventbus.EventBus

class LoginMessagingFragment : BaseAuthMessagingFragment() {
    private val TAG = "lgx_LoginMessagingFragment"
    lateinit var uiCommunicationListener: UICommunicationListener

    companion object {
        fun newInstance() = LoginMessagingFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_messaging, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiCommunicationListener.expandAppBar()
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.login_to_chat)


        setupActionBarWithNavController(R.id.loginFragment, activity as AppCompatActivity)

        val mPrefs: SharedPreferences = activity!!.getSharedPreferences("accountkey", Context.MODE_PRIVATE)
        val accountEmailText = mPrefs.getString("accountEmail", "default")

        input_email.setText("${accountEmailText}")
        input_email.isEnabled = true

        login_button.setOnClickListener {
            login()
        }
        issueLayout.cancelImage.setOnClickListener {
            issueLayout.visibility = View.GONE
        }

//        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
//            login()
//            true
//        }

    }

    private fun login() {
        EventBus.getDefault().post(KeyboardEvent())
        if (input_password.text.toString().length < 6) {
            //name or password doesn't match format
            Toast.makeText(context, "Password should be greater than 6", Toast.LENGTH_LONG)
                .show()
        } else {

            //All fields are correct we can login
            loginMessagingViewModel.login(
                AuthUtil.firebaseAuthInstance,
                input_email.text.toString(),
                input_password.text.toString()
            ).observe(viewLifecycleOwner, Observer { loadState ->

                when (loadState) {
                    LoadState.SUCCESS -> {   //triggered when login with email and password is successful
//
                        navMainActivity()
                        Toast.makeText(context, "Login successful", Toast.LENGTH_LONG).show()
                        loginMessagingViewModel.doneNavigating()
                    }
                    LoadState.LOADING -> {
                        loadingLayout.visibility = View.VISIBLE
                        issueLayout.visibility = View.GONE
                    }
                    LoadState.FAILURE -> {
                        loadingLayout.visibility = View.GONE
                        issueLayout.visibility = View.VISIBLE
                        issueLayout.textViewIssue.text = ErrorMessage.errorMessage
                    }
                }
            })

        }
    }


    fun setupActionBarWithNavController(fragmentId: Int, activity: AppCompatActivity) {
        val appBarConfiguration = AppBarConfiguration(setOf(fragmentId))
        NavigationUI.setupActionBarWithNavController(
            activity,
            findNavController(),
            appBarConfiguration
        )
    }

    @SuppressLint("LongLogTag")
    fun navMainActivity() {
        activity?.let {
            val intent = Intent(it, MainActivity::class.java)
            it.startActivity(intent)
        }
        activity?.finish()
    }

    @SuppressLint("LongLogTag")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uiCommunicationListener = context as UICommunicationListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement DataStateChangeListener")
        }

    }
}
