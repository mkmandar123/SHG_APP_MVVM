package com.digitaldetox.aww.api.main.responses

import com.digitaldetox.aww.models.UserloanrequestRoom
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserloanrequestCreateUpdateResponse(

    @SerializedName("body")
    @Expose
    val body: String,

    @SerializedName("created")
    @Expose
    val created: String,

    @SerializedName("pk")
    @Expose
    val pk: Int,

    @SerializedName("loanamount")
    @Expose
    val loanamount: Int,

    @SerializedName("authorsender")
    @Expose
    val authorsender: String,

    @SerializedName("authorsender_username")
    @Expose
    val authorsenderUsername: String,

    @SerializedName("subreddit")
    @Expose
    val subreddit: String,

    @SerializedName("subreddit_title")
    @Expose
    val subredditTitle: String,

    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("updated")
    @Expose
    val updated: String



) {
    fun toUserloanrequestroom(): UserloanrequestRoom {
        return UserloanrequestRoom(
            pk = pk,
            loanamount = loanamount,
            title = title,
            body = body,
            authorsender = authorsender,
            subreddit = subreddit,
            albumId = "dummyalbumid_subreddit_create"
        )
    }
}

