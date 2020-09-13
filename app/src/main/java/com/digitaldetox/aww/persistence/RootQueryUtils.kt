package com.digitaldetox.aww.persistence

import com.digitaldetox.aww.models.SubredditRoom
import com.digitaldetox.aww.models.UserloanrequestRoom
import com.digitaldetox.aww.models.UsersavingrequestRoom
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.ORDER_BY_ASC_DATE_UPDATED_SUBREDDIT
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.ORDER_BY_ASC_DATE_UPDATED_USERLOANREQUEST
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.ORDER_BY_ASC_DATE_UPDATED_USERSAVINGREQUEST
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.ORDER_BY_ASC_USERNAME_SUBREDDIT
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.ORDER_BY_ASC_USERNAME_USERLOANREQUEST
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.ORDER_BY_ASC_USERNAME_USERSAVINGREQUEST
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.ORDER_BY_DESC_DATE_UPDATED_SUBREDDIT
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.ORDER_BY_DESC_DATE_UPDATED_USERLOANREQUEST
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.ORDER_BY_DESC_DATE_UPDATED_USERSAVINGREQUEST
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.ORDER_BY_DESC_USERNAME_SUBREDDIT
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.ORDER_BY_DESC_USERNAME_USERLOANREQUEST
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.ORDER_BY_DESC_USERNAME_USERSAVINGREQUEST

class RootQueryUtils {

    companion object {
        private val TAG: String = "AppDebug"

        const val HUMANSAVINGPROFILE_ORDER_ASC: String = ""
        const val HUMANSAVINGPROFILE_ORDER_DESC: String = "-"
        const val HUMANSAVINGPROFILE_FILTER_USERNAME = "title"
        const val HUMANSAVINGPROFILE_FILTER_DATE_UPDATED = "pk"

        val ORDER_BY_ASC_DATE_UPDATED_HUMANSAVINGPROFILE =
            HUMANSAVINGPROFILE_ORDER_ASC + HUMANSAVINGPROFILE_FILTER_DATE_UPDATED
        val ORDER_BY_DESC_DATE_UPDATED_HUMANSAVINGPROFILE =
            HUMANSAVINGPROFILE_ORDER_DESC + HUMANSAVINGPROFILE_FILTER_DATE_UPDATED
        val ORDER_BY_ASC_USERNAME_HUMANSAVINGPROFILE = HUMANSAVINGPROFILE_ORDER_ASC + HUMANSAVINGPROFILE_FILTER_USERNAME
        val ORDER_BY_DESC_USERNAME_HUMANSAVINGPROFILE = HUMANSAVINGPROFILE_ORDER_DESC + HUMANSAVINGPROFILE_FILTER_USERNAME


        const val HUMANLOANPROFILE_ORDER_ASC: String = ""
        const val HUMANLOANPROFILE_ORDER_DESC: String = "-"
        const val HUMANLOANPROFILE_FILTER_USERNAME = "title"
        const val HUMANLOANPROFILE_FILTER_DATE_UPDATED = "pk"

        val ORDER_BY_ASC_DATE_UPDATED_HUMANLOANPROFILE =
            HUMANLOANPROFILE_ORDER_ASC + HUMANLOANPROFILE_FILTER_DATE_UPDATED
        val ORDER_BY_DESC_DATE_UPDATED_HUMANLOANPROFILE =
            HUMANLOANPROFILE_ORDER_DESC + HUMANLOANPROFILE_FILTER_DATE_UPDATED
        val ORDER_BY_ASC_USERNAME_HUMANLOANPROFILE = HUMANLOANPROFILE_ORDER_ASC + HUMANLOANPROFILE_FILTER_USERNAME
        val ORDER_BY_DESC_USERNAME_HUMANLOANPROFILE = HUMANLOANPROFILE_ORDER_DESC + HUMANLOANPROFILE_FILTER_USERNAME

        const val SUBREDDIT_ORDER_ASC: String = ""
        const val SUBREDDIT_ORDER_DESC: String = "-"
        const val SUBREDDIT_FILTER_USERNAME = "title"
        const val SUBREDDIT_FILTER_DATE_UPDATED = "pk"

        val ORDER_BY_ASC_DATE_UPDATED_SUBREDDIT =
            SUBREDDIT_ORDER_ASC + SUBREDDIT_FILTER_DATE_UPDATED
        val ORDER_BY_DESC_DATE_UPDATED_SUBREDDIT =
            SUBREDDIT_ORDER_DESC + SUBREDDIT_FILTER_DATE_UPDATED
        val ORDER_BY_ASC_USERNAME_SUBREDDIT = SUBREDDIT_ORDER_ASC + SUBREDDIT_FILTER_USERNAME
        val ORDER_BY_DESC_USERNAME_SUBREDDIT = SUBREDDIT_ORDER_DESC + SUBREDDIT_FILTER_USERNAME

        /* SUBUSER */

        const val SUBUSER_ORDER_ASC: String = ""
        const val SUBUSER_ORDER_DESC: String = "-"
        const val SUBUSER_FILTER_USERNAME = "title"
        const val SUBUSER_FILTER_DATE_UPDATED = "pk"

        val ORDER_BY_ASC_DATE_UPDATED_SUBUSER = SUBUSER_ORDER_ASC + SUBUSER_FILTER_DATE_UPDATED
        val ORDER_BY_DESC_DATE_UPDATED_SUBUSER = SUBUSER_ORDER_DESC + SUBUSER_FILTER_DATE_UPDATED
        val ORDER_BY_ASC_USERNAME_SUBUSER = SUBUSER_ORDER_ASC + SUBUSER_FILTER_USERNAME
        val ORDER_BY_DESC_USERNAME_SUBUSER = SUBUSER_ORDER_DESC + SUBUSER_FILTER_USERNAME

        /* end SUBUSER */

        /* USERLOANREQUEST */

        const val USERLOANREQUEST_ORDER_ASC: String = ""
        const val USERLOANREQUEST_ORDER_DESC: String = "-"
        const val USERLOANREQUEST_FILTER_USERNAME = "title"
        const val USERLOANREQUEST_FILTER_DATE_UPDATED = "pk"

        val ORDER_BY_ASC_DATE_UPDATED_USERLOANREQUEST =
            USERLOANREQUEST_ORDER_ASC + USERLOANREQUEST_FILTER_DATE_UPDATED
        val ORDER_BY_DESC_DATE_UPDATED_USERLOANREQUEST =
            USERLOANREQUEST_ORDER_DESC + USERLOANREQUEST_FILTER_DATE_UPDATED
        val ORDER_BY_ASC_USERNAME_USERLOANREQUEST =
            USERLOANREQUEST_ORDER_ASC + USERLOANREQUEST_FILTER_USERNAME
        val ORDER_BY_DESC_USERNAME_USERLOANREQUEST =
            USERLOANREQUEST_ORDER_DESC + USERLOANREQUEST_FILTER_USERNAME

        /* end USERLOANREQUEST */

        /* USERSAVINGREQUEST */

        const val USERSAVINGREQUEST_ORDER_ASC: String = ""
        const val USERSAVINGREQUEST_ORDER_DESC: String = "-"
        const val USERSAVINGREQUEST_FILTER_USERNAME = "title"
        const val USERSAVINGREQUEST_FILTER_DATE_UPDATED = "pk"

        val ORDER_BY_ASC_DATE_UPDATED_USERSAVINGREQUEST =
            USERSAVINGREQUEST_ORDER_ASC + USERSAVINGREQUEST_FILTER_DATE_UPDATED
        val ORDER_BY_DESC_DATE_UPDATED_USERSAVINGREQUEST =
            USERSAVINGREQUEST_ORDER_DESC + USERSAVINGREQUEST_FILTER_DATE_UPDATED
        val ORDER_BY_ASC_USERNAME_USERSAVINGREQUEST =
            USERSAVINGREQUEST_ORDER_ASC + USERSAVINGREQUEST_FILTER_USERNAME
        val ORDER_BY_DESC_USERNAME_USERSAVINGREQUEST =
            USERSAVINGREQUEST_ORDER_DESC + USERSAVINGREQUEST_FILTER_USERNAME

/* end USERSAVINGREQUEST */
    }
}


suspend fun SubredditDao.returnOrderedSubredditQuery(
    query: String,
    filterAndOrder: String,
    page: Int
): List<SubredditRoom> {

    when {

        filterAndOrder.contains(ORDER_BY_DESC_DATE_UPDATED_SUBREDDIT) -> {
            return searchSubredditroomsOrderByDateDESC(
                query = query,
                page = page
            )
        }

        filterAndOrder.contains(ORDER_BY_ASC_DATE_UPDATED_SUBREDDIT) -> {
            return searchSubredditroomsOrderByDateASC(
                query = query,
                page = page
            )
        }

        filterAndOrder.contains(ORDER_BY_DESC_USERNAME_SUBREDDIT) -> {
            return searchSubredditroomsOrderByAuthorDESC(
                query = query,
                page = page
            )
        }

        filterAndOrder.contains(ORDER_BY_ASC_USERNAME_SUBREDDIT) -> {
            return searchSubredditroomsOrderByAuthorASC(
                query = query,
                page = page
            )
        }
        else ->
            return searchSubredditroomsOrderByDateDESC(
                query = query,
                page = page
            )
    }
}

/* USERLOANREQUEST */

suspend fun UserloanrequestDao.returnOrderedUserloanrequestQuery(
    query: String,
    filterAndOrder: String,
    page: Int,
    authorsender: String
): List<UserloanrequestRoom> {

    when {

        filterAndOrder.contains(ORDER_BY_DESC_DATE_UPDATED_USERLOANREQUEST) -> {
            return searchUserloanrequestsOrderByDateDESC(
                query = query,
                page = page,
                authorsender = authorsender
            )
        }

        filterAndOrder.contains(ORDER_BY_ASC_DATE_UPDATED_USERLOANREQUEST) -> {
            return searchUserloanrequestsOrderByDateASC(
                query = query,
                page = page,
                authorsender = authorsender
            )
        }

        filterAndOrder.contains(ORDER_BY_DESC_USERNAME_USERLOANREQUEST) -> {
            return searchUserloanrequestsOrderByAuthorDESC(
                query = query,
                page = page,
                authorsender = authorsender
            )
        }

        filterAndOrder.contains(ORDER_BY_ASC_USERNAME_USERLOANREQUEST) -> {
            return searchUserloanrequestsOrderByAuthorASC(
                query = query,
                page = page,
                authorsender = authorsender
            )
        }
        else ->
            return searchUserloanrequestsOrderByDateDESC(
                query = query,
                page = page,
                authorsender = authorsender
            )
    }
}

/* end USERLOANREQUEST */

/* USERSAVINGREQUEST */

suspend fun UsersavingrequestDao.returnOrderedUsersavingrequestQuery(
    query: String,
    filterAndOrder: String,
    page: Int,
    authorsender: String
): List<UsersavingrequestRoom> {

    when {

        filterAndOrder.contains(ORDER_BY_DESC_DATE_UPDATED_USERSAVINGREQUEST) -> {
            return searchUsersavingrequestsOrderByDateDESC(
                query = query,
                page = page,
                authorsender = authorsender
            )
        }

        filterAndOrder.contains(ORDER_BY_ASC_DATE_UPDATED_USERSAVINGREQUEST) -> {
            return searchUsersavingrequestsOrderByDateASC(
                query = query,
                page = page,
                authorsender = authorsender
            )
        }

        filterAndOrder.contains(ORDER_BY_DESC_USERNAME_USERSAVINGREQUEST) -> {
            return searchUsersavingrequestsOrderByAuthorDESC(
                query = query,
                page = page,
                authorsender = authorsender
            )
        }

        filterAndOrder.contains(ORDER_BY_ASC_USERNAME_USERSAVINGREQUEST) -> {
            return searchUsersavingrequestsOrderByAuthorASC(
                query = query,
                page = page,
                authorsender = authorsender
            )
        }
        else ->
            return searchUsersavingrequestsOrderByDateDESC(
                query = query,
                page = page,
                authorsender = authorsender
            )
    }
}

/* end USERSAVINGREQUEST */