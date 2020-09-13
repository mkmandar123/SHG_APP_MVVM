package com.digitaldetox.aww.ui.auth_messaging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.digitaldetox.aww.R
import com.digitaldetox.aww.messagingmvvm.fragments.login.LoginMessagingViewModel
import com.digitaldetox.aww.messagingmvvm.fragments.register.SignupMessagingViewModel


abstract class BaseAuthMessagingFragment : Fragment() {

    lateinit var loginMessagingViewModel: LoginMessagingViewModel
    lateinit var signUpMessagingViewModel: SignupMessagingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_auth_messaging, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginMessagingViewModel = activity?.run {
            ViewModelProvider(this).get(LoginMessagingViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        signUpMessagingViewModel = activity?.run {
            ViewModelProvider(this).get(SignupMessagingViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }
}
