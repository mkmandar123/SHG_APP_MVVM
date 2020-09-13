package com.digitaldetox.aww.ui.main.create_userloanrequest.state

import com.digitaldetox.aww.util.StateEvent

sealed class CreateUserloanrequestStateEvent: StateEvent {

    data class CreateNewUserloanrequestEvent(
        val title: String,
        val body: String,
        val loanamount: Int,
        val subreddit: String,
        val authorsender: String
    ): CreateUserloanrequestStateEvent() {
        override fun errorInfo(): String {
            return "Unable to create a new userpost post."
        }

        override fun toString(): String {
            return "CreateUserloanrequestStateEvent"
        }
    }

    class None: CreateUserloanrequestStateEvent() {
        override fun errorInfo(): String {
            return "None."
        }
    }
}