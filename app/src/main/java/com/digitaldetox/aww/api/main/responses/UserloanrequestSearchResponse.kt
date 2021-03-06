package com.digitaldetox.aww.api.main.responses

import com.digitaldetox.aww.models.UserloanrequestRoom
import com.digitaldetox.aww.util.Constants.Companion.ALBUM_ID_CONSTANT
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserloanrequestSearchResponse(
    @SerializedName("body")
    @Expose
    val body: String,

    @SerializedName("pk")
    @Expose
    val pk: Int,


    @SerializedName("loanamount")
    @Expose
    val loanamount: Int,

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
    fun toUserloanrequestroom(): UserloanrequestRoom {
        return UserloanrequestRoom(
            pk = pk,
            loanamount = loanamount,
            title = title,
            body = body,
            authorsender = authorsender,
            subreddit = subreddit,
            albumId = ALBUM_ID_CONSTANT
        )
    }

    override fun toString(): String {
        return "UserloanrequestSearchResponse(body='$body', pk=$pk, loanamount=$loanamount, authorsender='$authorsender', subreddit='$subreddit', title='$title')"
    }


}
