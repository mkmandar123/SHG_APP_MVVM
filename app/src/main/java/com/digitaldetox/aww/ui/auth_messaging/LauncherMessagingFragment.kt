package com.digitaldetox.aww.ui.auth_messaging


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.digitaldetox.aww.R
import com.digitaldetox.aww.ui.main.account.BaseAccountFragment
import com.digitaldetox.aww.util.Lgx

import kotlinx.android.synthetic.main.fragment_launcher_messaging.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject


class LauncherMessagingFragment : BaseAuthMessagingFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launcher_messaging, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.chat_registration)
        val mPrefs: SharedPreferences = activity!!.getSharedPreferences("accountkey", Context.MODE_PRIVATE)
        val accountEmailText = mPrefs.getString("accountEmail", "default")
//        Toast.makeText(context, "launcher pref: ${accountEmailText}", Toast.LENGTH_SHORT).show()

        register.setOnClickListener {
            Lgx.d("LGX", "register clicked")
            navRegistration()
        }

        login.setOnClickListener {
            navLogin()
        }

        app_logo.setOnClickListener {
            guestLogin()
        }

        forgot_password.setOnClickListener {
            navForgotPassword()
        }

        focusable_view.requestFocus() // reset focus
    }

    fun guestLogin(){
    }
    fun navLogin(){
        findNavController().navigate(R.id.action_launcherMessagingFragment_to_loginMessagingFragment)
    }

    fun navRegistration(){
        findNavController().navigate(R.id.action_launcherMessagingFragment_to_signUpMessagingFragment)
    }

    fun navForgotPassword(){
    }


}
