package com.digitaldetox.aww.messagingmvvm.fragments.register

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.digitaldetox.aww.R
import com.digitaldetox.aww.messagingmvvm.util.AuthUtil
import com.digitaldetox.aww.messagingmvvm.util.ErrorMessage
import com.digitaldetox.aww.messagingmvvm.util.LoadState
import com.digitaldetox.aww.messagingmvvm.util.eventbus_events.KeyboardEvent
import com.digitaldetox.aww.ui.UICommunicationListener
import com.digitaldetox.aww.ui.auth_messaging.BaseAuthMessagingFragment
import com.digitaldetox.aww.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login_messaging.*
import kotlinx.android.synthetic.main.fragment_signup_messaging.*
import kotlinx.android.synthetic.main.fragment_signup_messaging.input_email
import kotlinx.android.synthetic.main.fragment_signup_messaging.input_password
import kotlinx.android.synthetic.main.fragment_signup_messaging.issueLayout
import kotlinx.android.synthetic.main.fragment_signup_messaging.loadingLayout
import kotlinx.android.synthetic.main.issue_layout.view.*
import org.greenrobot.eventbus.EventBus
import java.util.regex.Matcher
import java.util.regex.Pattern

private const val TAG = "SignupMessagingFragment"

class SignupMessagingFragment : BaseAuthMessagingFragment() {

    private lateinit var pattern: Pattern
    lateinit var uiCommunicationListener: UICommunicationListener

    companion object {
        fun newInstance() = SignupMessagingFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_messaging, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiCommunicationListener.expandAppBar()
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.sign_up_to_chat)
        //regex pattern to check email format
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)\$"
        pattern = Pattern.compile(emailRegex)




        val mPrefs: SharedPreferences = activity!!.getSharedPreferences("accountkey", Context.MODE_PRIVATE)
        val accountEmailText = mPrefs.getString("accountEmail", "default")
        val accountUsernameText = mPrefs.getString("accountUsername", "default")

        input_email.setText("${accountEmailText}")
        input_email.isEnabled = true
        input_username.setText("${accountUsernameText}")
        input_username.isEnabled = true

        //handle register click
        register_button.setOnClickListener {

            signUp()

        }


        //hide issue layout on x icon click
        issueLayout.cancelImage.setOnClickListener {
            issueLayout.visibility = View.GONE
        }

        //show proper loading/error ui
        signUpMessagingViewModel.loadingState.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoadState.LOADING -> {
                    loadingLayout.visibility = View.VISIBLE
                    issueLayout.visibility = View.GONE
                }
                LoadState.SUCCESS -> {
                    loadingLayout.visibility = View.GONE
                    issueLayout.visibility = View.GONE
                }
                LoadState.FAILURE -> {
                    loadingLayout.visibility = View.GONE
                    issueLayout.visibility = View.VISIBLE
                    issueLayout.textViewIssue.text = ErrorMessage.errorMessage
                }

            }
        })


        //sign up on keyboard done click when focus is on passwordEditText
        input_password.setOnEditorActionListener { _, actionId, _ ->
            signUp()
            true
        }
    }

    private fun signUp() {
        EventBus.getDefault().post(KeyboardEvent())


        //email and pass are matching requirements now we can register to firebase auth

        if (input_password.text.toString().length < 6 || input_email.text.toString()
                .isNullOrEmpty() || input_email.text.toString().isNullOrEmpty()
        ) {

            Toast.makeText(context, "Password must be greater than 6 charcters", Toast.LENGTH_SHORT)
                .show()
        }else{



        signUpMessagingViewModel.registerEmail(
            AuthUtil.firebaseAuthInstance,
            input_email.text.toString(),
            input_password.text.toString(),
            input_email.text.toString()
        )


        signUpMessagingViewModel.navigateToHomeMutableLiveData.observe(
            viewLifecycleOwner,
            Observer { navigateToHome ->
                if (navigateToHome != null && navigateToHome) {
                    navMainActivity()
                    Toast.makeText(context, "Sign up successful", Toast.LENGTH_LONG).show()
                    signUpMessagingViewModel.doneNavigating()
                }
            })
        }
    }

    @SuppressLint("LongLogTag")
    fun navMainActivity() {
        activity?.let {
            val intent = Intent(it, MainActivity::class.java)
            it.startActivity(intent)
            it.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            it.finish()
        }

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
