package com.digitaldetox.aww.api.main.responses

import com.digitaldetox.aww.models.HumanloanprofileRoom
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class HumanloanprofileListSearchResponse(

    @SerializedName("loanrequests")
    @Expose
    var loanrequests: List<HumanloanprofileSearchResponse>,

    @SerializedName("detail")
    @Expose
    var detail: String
) {

    fun toList(): List<HumanloanprofileRoom>{
        val humanloanprofileRoomList: ArrayList<HumanloanprofileRoom> = ArrayList()
        for( humanloanprofileResponse in loanrequests){
            humanloanprofileRoomList.add(
                humanloanprofileResponse.toHumanloanprofileroom()
            )
        }
        return humanloanprofileRoomList
    }

    override fun toString(): String {
        return "HumanloanprofileListSearchResponse(loanrequests=$loanrequests, detail='$detail')"
    }


}