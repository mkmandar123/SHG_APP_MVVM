package com.digitaldetox.aww.api.main.responses

import com.digitaldetox.aww.models.UsersavingrequestRoom
import com.digitaldetox.aww.util.Constants.Companion.ALBUM_ID_CONSTANT
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UsersavingrequestSearchResponse(
    @SerializedName("body")
    @Expose
    val body: String,

    @SerializedName("pk")
    @Expose
    val pk: Int,

    @SerializedName("savingamount")
    @Expose
    val savingamount: Int,

    @SerializedName("authorsender")
    @Expose
    val authorsender: String,

    @SerializedName("subreddit")
    @Expose
    val subreddit: String,

    @SerializedName("title")
    @Expose
    val title: String


) {
    fun toUsersavingrequestroom(): UsersavingrequestRoom {
        return UsersavingrequestRoom(
            pk = pk,
            title = title,
            body = body,
            savingamount = savingamount,
            authorsender = authorsender,
            subreddit = subreddit,
            albumId = ALBUM_ID_CONSTANT
        )
    }

    override fun toString(): String {
        return "UsersavingrequestSearchResponse(body='$body', pk=$pk, savingamount=$savingamount, authorsender='$authorsender', subreddit='$subreddit', title='$title')"
    }


}
