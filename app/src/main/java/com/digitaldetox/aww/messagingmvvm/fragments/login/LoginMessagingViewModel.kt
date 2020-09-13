package com.digitaldetox.aww.messagingmvvm.fragments.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.digitaldetox.aww.messagingmvvm.util.ErrorMessage
import com.digitaldetox.aww.messagingmvvm.util.LoadState
import java.util.regex.Matcher
import java.util.regex.Pattern


class LoginMessagingViewModel : ViewModel() {


    private val loadingState = MutableLiveData<LoadState>()

    val emailMatch = MutableLiveData<Boolean>()
    val passwordMatch = MutableLiveData<Boolean>()

    private val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)\$"

    fun isEmailFormatCorrect(it: String): LiveData<Boolean> {

        val pattern: Pattern = Pattern.compile(emailRegex)
        val matcher: Matcher = pattern.matcher(it)
        emailMatch.value = matcher.matches()

        return emailMatch
    }

    fun isPasswordFormatCorrect(it: String): LiveData<Boolean> {

        val lengthOfPassword    = 6
        passwordMatch.value = lengthOfPassword <= it.length
//        val matcher: Matcher = pattern.matcher(it)
//        emailMatch.value = matcher.matches()

        return passwordMatch
    }



    fun login(auth: FirebaseAuth, email: String, password: String): LiveData<LoadState> {
        loadingState.value = LoadState.LOADING

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            loadingState.value = LoadState.SUCCESS
        }.addOnFailureListener {
            ErrorMessage.errorMessage = it.message
            loadingState.value = LoadState.FAILURE
        }
        return loadingState
    }

    fun doneNavigating() {
        loadingState.value = null
    }

}
