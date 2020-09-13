package com.digitaldetox.aww.ui.auth


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.digitaldetox.aww.R
import com.digitaldetox.aww.di.auth.AuthScope
import com.digitaldetox.aww.ui.auth.state.AuthStateEvent.*
import com.digitaldetox.aww.ui.auth.state.RegistrationFields
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class RegisterFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory
) : BaseAuthFragment(R.layout.fragment_register, viewModelFactory) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register_button.setOnClickListener {
            register()
        }
        subscribeObservers()
    }

    fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.registrationFields?.let {
                it.registration_email?.let { input_email.setText(it) }
                it.registration_username?.let { input_username.setText(it) }
                it.registration_password?.let { input_password.setText(it) }
            }
        })
    }

    fun register() {
        viewModel.setStateEvent(
            RegisterAttemptEvent(
                email = input_email.text.toString(),
                username = input_username.text.toString(),
                password = input_password.text.toString(),
                first_name = input_firstname.text.toString(),
                last_name = input_lastname.text.toString(),
                location = input_location.text.toString(),
                aadharcard = input_aadharcard.text.toString(),
                age = input_age.text.toString().toInt(),
                savingtarget = input_savingtarget.text.toString().toInt()
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRegistrationFields(
            RegistrationFields(
                input_email.text.toString(),
                input_username.text.toString(),
                input_password.text.toString()
            )
        )
    }
}