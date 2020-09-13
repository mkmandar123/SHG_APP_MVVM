package com.digitaldetox.aww.ui.main.account.state

import com.digitaldetox.aww.util.StateEvent

sealed class AccountStateEvent: StateEvent {

    class GetAccountPropertiesEvent: AccountStateEvent() {

        override fun errorInfo(): String {
            return "Error retrieving account properties."
        }

        override fun toString(): String {
            return "GetAccountPropertiesEvent"
        }
    }

    data class UpdateAccountPropertiesEvent(
        val email: String,
        val username: String,
        val first_name: String,
        val last_name: String,
        val location: String,
        val age: Int,
        val savingtarget: Int
    ): AccountStateEvent() {

        override fun errorInfo(): String {
            return "Error updating account properties."
        }

        override fun toString(): String {
            return "UpdateAccountPropertiesEvent"
        }
    }

    data class ChangePasswordEvent(
        val currentPassword: String,
        val newPassword: String,
        val confirmNewPassword: String
    ) : AccountStateEvent() {

        override fun errorInfo(): String {
            return "Error changing password."
        }

        override fun toString(): String {
            return "ChangePasswordEvent"
        }
    }

    class None: AccountStateEvent() {
        override fun errorInfo(): String {
            return "None"
        }
    }
}