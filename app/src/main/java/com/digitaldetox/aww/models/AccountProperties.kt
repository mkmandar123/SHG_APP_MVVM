package com.digitaldetox.aww.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "account_properties")
data class AccountProperties(

    @SerializedName("pk")
    @Expose
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pk") var pk: Int,

    @SerializedName("email")
    @Expose
    @ColumnInfo(name = "email") var email: String,

    @SerializedName("username")
    @Expose
    @ColumnInfo(name = "username") var username: String,

    @SerializedName("first_name")
    @Expose
    @ColumnInfo(name = "first_name") var first_name: String,

    @SerializedName("last_name")
    @Expose
    @ColumnInfo(name = "last_name") var last_name: String,

    @SerializedName("location")
    @Expose
    @ColumnInfo(name = "location") var location: String,

    @SerializedName("aadharcard")
    @Expose
    @ColumnInfo(name = "aadharcard") var aadharcard: String,

    @SerializedName("age")
    @Expose
    @ColumnInfo(name = "age") var age: Int,

    @SerializedName("savingtarget")
    @Expose
    @ColumnInfo(name = "savingtarget") var savingtarget: Int


) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as AccountProperties

        if (pk != other.pk) return false
        if (email != other.email) return false
        if (username != other.username) return false
        if (first_name != other.first_name) return false
        if (last_name != other.last_name) return false
        if (location != other.location) return false
        if (aadharcard != other.aadharcard) return false
        if (age != other.age) return false
        if (savingtarget != other.savingtarget) return false

        return true
    }

}











