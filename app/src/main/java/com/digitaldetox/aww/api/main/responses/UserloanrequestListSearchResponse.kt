package com.digitaldetox.aww.api.main.responses

import com.digitaldetox.aww.models.UserloanrequestRoom
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserloanrequestListSearchResponse(

    @SerializedName("results")
    @Expose
    val results: List<UserloanrequestSearchResponse>

) {
    fun toList(): List<UserloanrequestRoom>{
        val userloanrequestRoomList: ArrayList<UserloanrequestRoom> = ArrayList()
        for( userloanrequestResponse in results){
            userloanrequestRoomList.add(
                userloanrequestResponse.toUserloanrequestroom()
            )
        }
        return userloanrequestRoomList
    }

    override fun toString(): String {
        return "UserloanrequestListSearchResponse(results=$results)"
    }


}