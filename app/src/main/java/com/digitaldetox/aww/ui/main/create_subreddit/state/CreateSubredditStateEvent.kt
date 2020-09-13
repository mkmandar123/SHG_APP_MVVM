package com.digitaldetox.aww.ui.main.create_subreddit.state

import com.digitaldetox.aww.util.StateEvent


sealed class CreateSubredditStateEvent: StateEvent {

    data class CreateNewSubredditEvent(
        val title: String,
        val body: String
    ): CreateSubredditStateEvent() {
        override fun errorInfo(): String {
            return "Unable to create a new subreddit post."
        }

        override fun toString(): String {
            return "CreateSubredditStateEvent"
        }
    }

    class None: CreateSubredditStateEvent() {
        override fun errorInfo(): String {
            return "None."
        }
    }
}