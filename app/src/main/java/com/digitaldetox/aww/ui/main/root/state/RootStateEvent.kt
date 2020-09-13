package com.digitaldetox.aww.ui.main.root.state

import com.digitaldetox.aww.util.StateEvent

sealed class RootStateEvent : StateEvent {

    class HumansavingprofileSearchEvent(
        val clearLayoutManagerState: Boolean = true
    ) : RootStateEvent() {
        override fun errorInfo(): String {
            return "Error searching for humansavingprofile posts."
        }

        override fun toString(): String {
            return "HumansavingprofileSearchEvent"
        }
    }

    class HumanloanprofileSearchEvent(
        val clearLayoutManagerState: Boolean = true
    ) : RootStateEvent() {
        override fun errorInfo(): String {
            return "Error searching for humanloanprofile posts."
        }

        override fun toString(): String {
            return "HumanloanprofileSearchEvent"
        }
    }

    class SubredditSearchEvent(
        val clearLayoutManagerState: Boolean = true
    ) : RootStateEvent() {
        override fun errorInfo(): String {
            return "Error searching for subreddit posts."
        }

        override fun toString(): String {
            return "SubredditSearchEvent"
        }
    }

    class DeleteSubredditPostEvent : RootStateEvent() {
        override fun errorInfo(): String {
            return "Error deleting that subreddit post."
        }

        override fun toString(): String {
            return "DeleteSubredditPostEvent"
        }
    }

    data class UpdateSubredditPostEvent(
        val title: String,
        val body: String
    ) : RootStateEvent() {
        override fun errorInfo(): String {
            return "Error updating that subreddit post."
        }

        override fun toString(): String {
            return "UpdateSubredditPostEvent"
        }

    }

    class SubuserSearchEvent(
        val clearLayoutManagerState: Boolean = true
    ) : RootStateEvent() {
        override fun errorInfo(): String {
            return "Error searching for subuser posts."
        }

        override fun toString(): String {
            return "SubuserSearchEvent"
        }
    }

    /* USERLOANREQUEST */

    class UserloanrequestSearchEvent(
        val clearLayoutManagerState: Boolean = true
    ) : RootStateEvent() {
        override fun errorInfo(): String {
            return "Error searching for userpost posts."
        }

        override fun toString(): String {
            return "UserloanrequestSearchEvent"
        }
    }

    class DeleteUserloanrequestPostEvent : RootStateEvent() {
        override fun errorInfo(): String {
            return "Error deleting that userpost post."
        }

        override fun toString(): String {
            return "DeleteUserloanrequestPostEvent"
        }
    }

    data class UpdateUserloanrequestPostEvent(
        val title: String,
        val body: String,
        val loanamount: Int
    ) : RootStateEvent() {
        override fun errorInfo(): String {
            return "Error updating that userpost post."
        }

        override fun toString(): String {
            return "UpdateUserloanrequestPostEvent"
        }

    }


    /* end USERLOANREQUEST */



/* USERSAVINGREQUEST */

    class UsersavingrequestSearchEvent(
        val clearLayoutManagerState: Boolean = true
    ) : RootStateEvent() {
        override fun errorInfo(): String {
            return "Error searching for userpost posts."
        }

        override fun toString(): String {
            return "UsersavingrequestSearchEvent"
        }
    }

    class DeleteUsersavingrequestPostEvent : RootStateEvent() {
        override fun errorInfo(): String {
            return "Error deleting that userpost post."
        }

        override fun toString(): String {
            return "DeleteUsersavingrequestPostEvent"
        }
    }

    data class UpdateUsersavingrequestPostEvent(
        val title: String,
        val body: String,
        val savingamount: Int
    ) : RootStateEvent() {
        override fun errorInfo(): String {
            return "Error updating that userpost post."
        }

        override fun toString(): String {
            return "UpdateUsersavingrequestPostEvent"
        }

    }


/* end USERSAVINGREQUEST */


    class None : RootStateEvent() {
        override fun errorInfo(): String {
            return "None."
        }
    }
}