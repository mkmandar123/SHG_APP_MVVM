package com.digitaldetox.aww.api.main.responses

import com.digitaldetox.aww.models.SubredditRoom
import com.digitaldetox.aww.util.Constants.Companion.ALBUM_ID_CONSTANT
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SubredditSearchResponse(

    @SerializedName("pk")
    @Expose
    var pk: Int,

    @SerializedName("title")
    @Expose
    var title: String,

    @SerializedName("memberscount")
    @Expose
    var memberscount: Int,



    @SerializedName("description")
    @Expose
    var description: String,

    @SerializedName("members")
    @Expose
    var members: ArrayList<String>? = null


) {
    fun toSubredditroom(): SubredditRoom {
        return SubredditRoom(
            pk = pk,
            memberscount = memberscount,
            title = title,
            description = description,
            members = members,
            albumId = ALBUM_ID_CONSTANT
        )
    }

    override fun toString(): String {
        return "SubredditSearchResponse(pk=$pk, title='$title', memberscount=$memberscount, description='$description', members=$members)"
    }


}














