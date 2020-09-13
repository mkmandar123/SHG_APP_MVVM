package com.digitaldetox.aww.api.main.responses

import com.digitaldetox.aww.models.SubredditRoom
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SubredditListSearchResponse(

    @SerializedName("results")
    @Expose
    var results: List<SubredditSearchResponse>,

    @SerializedName("detail")
    @Expose
    var detail: String
) {

    fun toList(): List<SubredditRoom>{
        val subredditRoomList: ArrayList<SubredditRoom> = ArrayList()
        for( subredditResponse in results){
            subredditRoomList.add(
                subredditResponse.toSubredditroom()
            )
        }
        return subredditRoomList
    }


    override fun toString(): String {
        return "SubredditListSearchResponse(results=$results, detail='$detail')"
    }
}