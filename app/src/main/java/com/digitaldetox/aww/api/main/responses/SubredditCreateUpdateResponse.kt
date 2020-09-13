package com.digitaldetox.aww.api.main.responses

import com.digitaldetox.aww.models.SubredditRoom
import com.digitaldetox.aww.util.Constants.Companion.ALBUM_ID_CONSTANT
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SubredditCreateUpdateResponse(

    @SerializedName("created")
    @Expose
    val created: String,

    @SerializedName("description")
    @Expose
    val description: String,

    @SerializedName("members")
    @Expose
    val members: ArrayList<String>,

    @SerializedName("moderators")
    @Expose
    val moderators: List<String>,

    @SerializedName("pk")
    @Expose
    val pk: Int,

    @SerializedName("title")
    @Expose
    val title: String


) {
    fun toSubredditroom(): SubredditRoom {
        return SubredditRoom(
            pk = pk,
            title = title,
            description = description,
            members = members,
            albumId = ALBUM_ID_CONSTANT
        )
    }
}












