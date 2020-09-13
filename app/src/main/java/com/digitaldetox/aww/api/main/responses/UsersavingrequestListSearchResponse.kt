package com.digitaldetox.aww.api.main.responses

import com.digitaldetox.aww.models.UsersavingrequestRoom
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UsersavingrequestListSearchResponse(

    @SerializedName("results")
    @Expose
    val results: List<UsersavingrequestSearchResponse>

) {
    fun toList(): List<UsersavingrequestRoom>{
        val usersavingrequestRoomList: ArrayList<UsersavingrequestRoom> = ArrayList()
        for( usersavingrequestResponse in results){
            usersavingrequestRoomList.add(
                usersavingrequestResponse.toUsersavingrequestroom()
            )
        }
        return usersavingrequestRoomList
    }

    override fun toString(): String {
        return "UsersavingrequestListSearchResponse(results=$results)"
    }


}