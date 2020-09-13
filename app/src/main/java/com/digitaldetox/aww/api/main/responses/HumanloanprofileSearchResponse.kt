package com.digitaldetox.aww.api.main.responses

import com.digitaldetox.aww.models.HumanloanprofileRoom
import com.digitaldetox.aww.util.Constants.Companion.ALBUM_ID_CONSTANT
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HumanloanprofileSearchResponse(

    @SerializedName("pk")
    @Expose
    var pk: Int,

    @SerializedName("title")
    @Expose
    var title: String 
 

) {
    fun toHumanloanprofileroom(): HumanloanprofileRoom {
        return HumanloanprofileRoom(
            pk = pk,
            title = title,
            albumId = ALBUM_ID_CONSTANT
        )
    }

    override fun toString(): String {
        return "HumanloanprofileSearchResponse(pk=$pk, title='$title')"
    }


}














