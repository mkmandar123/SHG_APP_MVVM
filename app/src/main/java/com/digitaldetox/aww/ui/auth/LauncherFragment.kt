package com.digitaldetox.aww.ui.auth


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.digitaldetox.aww.R
import com.digitaldetox.aww.di.auth.AuthScope
import com.digitaldetox.aww.ui.auth.state.AuthStateEvent
import kotlinx.android.synthetic.main.fragment_launcher.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class LauncherFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory
) : BaseAuthFragment(R.layout.fragment_launcher, viewModelFactory), TextToSpeech.OnInitListener {
    var tts: TextToSpeech?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tts = TextToSpeech(activity, this)



        name.text = getString(R.string.shg_app)

        name.setOnLongClickListener {
            allSpeech(name.text.toString())
            return@setOnLongClickListener true
        }

        tagline.text = getString(R.string.savings_first)


        tagline.setOnLongClickListener {
            allSpeech(tagline.text.toString())
            return@setOnLongClickListener true
        }


        whatIsAaharVihaarTextView.text = getString(R.string.lorem_ipsum_1_para_vivamus)
        termsAndConditionsTextView.text = getString(R.string.lorem_ipsum_2_para)

        register.setOnClickListener {
            navRegistration()
        }

        login.setOnClickListener {
            navLogin()
        }
        register.setOnLongClickListener {
            allSpeech(register.text.toString())
            return@setOnLongClickListener true
        }

        login.setOnLongClickListener {
            allSpeech(login.text.toString())
            return@setOnLongClickListener true
        }

        forgot_password.setOnClickListener {
            navForgotPassword()
        }

        app_logo.setOnClickListener {
            guestLogin()
        }
        goToAppButton.setOnClickListener{
            guestLogin()
        }

        focusable_view.requestFocus()
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.forLanguageTag("hin"))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }

    }

    override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    private fun allSpeech(stringSpeech: String){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts?.speak(stringSpeech,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            tts?.speak(stringSpeech, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    fun navLogin() {
        findNavController().navigate(R.id.action_launcherFragment_to_loginFragment)
    }

    fun navRegistration() {
        findNavController().navigate(R.id.action_launcherFragment_to_registerFragment)
    }

    fun navForgotPassword() {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.startupindia.gov.in/content/sih/en/privacy-policy.html"))
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








