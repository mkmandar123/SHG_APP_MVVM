package com.digitaldetox.aww.api.main.responses

import com.digitaldetox.aww.models.HumansavingprofileRoom
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class HumansavingprofileListSearchResponse(

    @SerializedName("savingrequests")
    @Expose
    var savingrequests: List<HumansavingprofileSearchResponse>,

    @SerializedName("detail")
    @Expose
    var detail: String
) {

    fun toList(): List<HumansavingprofileRoom>{
        val humansavingprofileRoomList: ArrayList<HumansavingprofileRoom> = ArrayList()
        for( humansavingprofileResponse in savingrequests){
            humansavingprofileRoomList.add(
                humansavingprofileResponse.toHumansavingprofileroom()
            )
        }
        return humansavingprofileRoomList
    }

    override fun toString(): String {
        return "HumansavingprofileListSearchResponse(savingrequests=$savingrequests, detail='$detail')"
    }


}