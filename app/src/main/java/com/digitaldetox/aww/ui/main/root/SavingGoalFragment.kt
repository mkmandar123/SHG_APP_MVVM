package com.digitaldetox.aww.ui.main.root


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.request.RequestOptions
import com.digitaldetox.aww.R
import com.digitaldetox.aww.ui.auth.state.AuthStateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class SavingGoalFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
    private val requestOptions: RequestOptions
) : BaseRootFragment(R.layout.fragment_saving_goal, viewModelFactory) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        name.text = "SHG App"
//        tagline.text = "Savings first — Credit later"
//
//
//
//        whatIsAaharVihaarTextView.text = getString(R.string.lorem_ipsum_1_para_vivamus)
//        termsAndConditionsTextView.text = getString(R.string.lorem_ipsum_2_para)

//        register.setOnClickListener {
//            navRegistration()
//        }
//
//        login.setOnClickListener {
//            navLogin()
//        }
//
//        forgot_password.setOnClickListener {
//            navForgotPassword()
//        }
//
//        app_logo.setOnClickListener {
//            guestLogin()
//        }
//        goToAppButton.setOnClickListener{
//            guestLogin()
//        }
//
//        focusable_view.requestFocus()
    }

    fun navLogin() {
        findNavController().navigate(R.id.action_launcherFragment_to_loginFragment)
    }

    fun navRegistration() {
        findNavController().navigate(R.id.action_launcherFragment_to_registerFragment)
    }

    fun navForgotPassword() {
        val i = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.startupindia.gov.in/content/sih/en/privacy-policy.html")
        )
        startActivity(i)
//        findNavController().navigate(R.id.action_launcherFragment_to_forgotPasswordFragment)
    }

    fun guestLogin() {
        viewModel.setStateEvent(
            AuthStateEvent.LoginAttemptEvent(
                "guest",
                "A123456z"
            )
        )
    }


}








