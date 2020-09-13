package com.digitaldetox.aww.api.main.responses

import com.digitaldetox.aww.models.HumansavingprofileRoom
import com.digitaldetox.aww.util.Constants.Companion.ALBUM_ID_CONSTANT
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HumansavingprofileSearchResponse(

    @SerializedName("pk")
    @Expose
    var pk: Int,

    @SerializedName("title")
    @Expose
    var title: String 
 

) {
    fun toHumansavingprofileroom(): HumansavingprofileRoom {
        return HumansavingprofileRoom(
            pk = pk,
            title = title,
            albumId = ALBUM_ID_CONSTANT
        )
    }

    override fun toString(): String {
        return "HumansavingprofileSearchResponse(pk=$pk, title='$title')"
    }


}














