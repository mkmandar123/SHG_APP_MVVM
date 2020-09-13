package com.digitaldetox.aww.util

class Constants {

    companion object {

        const val BASE_URL = "http://shipwor.herokuapp.com/"
        const val PASSWORD_RESET_URL: String = "http://shipwor.herokuapp.com/password_reset/"
        const val SIH_API_SUBREDDIT_UPLOAD_URL = "http://shipwor.herokuapp.com/subreddits/"

        const val SIH_API_USERLOANREQUEST_UPLOAD_URL =
            "http://shipwor.herokuapp.com/loanrequests/create/"
        const val SIH_API_USERLOANREQUEST_USERNAME_PARAM_UPLOAD_URL =
            "http://shipwor.herokuapp.com/loanrequestts/create/"

        const val SIH_API_USERSAVINGREQUEST_UPLOAD_URL =
            "http://shipwor.herokuapp.com/savingrequests/create/"
        const val SIH_API_USERSAVINGREQUEST_USERNAME_PARAM_UPLOAD_URL =
            "http://shipwor.herokuapp.com/savingrequestts/create/"


        const val NETWORK_TIMEOUT = 10000L
        const val CACHE_TIMEOUT = 2000L
        const val TESTING_NETWORK_DELAY = 0L // fake network delay for testing
        const val TESTING_CACHE_DELAY = 0L // fake cache delay for testing


        const val PAGINATION_PAGE_SIZE_SUBREDDIT = 10
        const val PAGINATION_PAGE_SIZE_USERLOANREQUEST = 10
        const val PAGINATION_PAGE_SIZE_USERSAVINGREQUEST = 10

        const val GALLERY_REQUEST_CODE = 201
        const val PERMISSIONS_REQUEST_READ_STORAGE: Int = 301
        const val CROP_IMAGE_INTENT_CODE: Int = 401

        const val ALBUM_ID_CONSTANT = "albumidconstant"

        const val SUBREDDIT_MEMBERS_KEY = "subredditmemberskey"
        const val SUBREDDIT_NAME_KEY = "subredditnamekey"
        const val USERLOANREQUEST_AUTHORSENDER_KEY = "userloanrequestauthorsenderkey"

        const val USERSAVINGREQUEST_AUTHORSENDER_KEY = "usersavingrequestauthorsenderkey"

    }
}