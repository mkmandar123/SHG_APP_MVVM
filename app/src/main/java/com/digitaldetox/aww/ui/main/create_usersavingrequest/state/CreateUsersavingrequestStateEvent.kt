package com.digitaldetox.aww.ui.main.create_usersavingrequest.state

import com.digitaldetox.aww.util.StateEvent

sealed class CreateUsersavingrequestStateEvent: StateEvent {

    data class CreateNewUsersavingrequestEvent(
        val title: String,
        val body: String,
        val savingamount: Int,
        val subreddit: String,
        val authorsender: String
    ): CreateUsersavingrequestStateEvent() {
        override fun errorInfo(): String {
            return "Unable to create a new userpost post."
        }

        override fun toString(): String {
            return "CreateUsersavingrequestStateEvent"
        }
    }

    class None: CreateUsersavingrequestStateEvent() {
        override fun errorInfo(): String {
            return "None."
        }
    }
}