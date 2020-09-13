package com.digitaldetox.aww.ui.auth.state

import com.digitaldetox.aww.util.StateEvent

sealed class AuthStateEvent: StateEvent {

    data class LoginAttemptEvent(
        val username: String,
        val password: String
    ): AuthStateEvent() {

        override fun errorInfo(): String {
            return "Login attempt failed."
        }

        override fun toString(): String {
            return "LoginStateEvent"
        }
    }

    data class RegisterAttemptEvent(
        val email: String,
        val username: String,
        val first_name: String,
        val last_name: String,
        val location: String,
        val aadharcard: String,
        val age: Int,
        val savingtarget: Int,
        val password: String
    ): AuthStateEvent(){

        override fun errorInfo(): String {
            return "Register attempt failed."
        }

        override fun toString(): String {
            return "RegisterAttemptEvent"
        }
    }

    class CheckPreviousAuthEvent(): AuthStateEvent() {

        override fun errorInfo(): String {
            return "Error checking for previously authenticated user. Please Clear Data from App Settings to fix the issue."
        }

        override fun toString(): String {
            return "CheckPreviousAuthEvent"
        }
    }

    class None: AuthStateEvent() {

        override fun errorInfo(): String {
            return "None"
        }

    }
}