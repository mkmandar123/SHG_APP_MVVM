package com.digitaldetox.aww.api.auth.network_responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse(

    @SerializedName("non_field_errors")
    @Expose
    var response_server: String?,

    @SerializedName("error_message")
    @Expose
    var errorMessage: String,

    @SerializedName("token")
    @Expose
    var token: String,

    @SerializedName("pk")
    @Expose
    var pk: Int,

    @SerializedName("username")
    @Expose
    var username: String,

    @SerializedName("first_name")
    @Expose
    var first_name: String,

    @SerializedName("last_name")
    @Expose
    var last_name: String,

    @SerializedName("location")
    @Expose
    var location: String,


    @SerializedName("aadharcard")
    @Expose
    var aadharcard: String,

    @SerializedName("age")
    @Expose
    var age: Int,

   @SerializedName("savingtarget")
    @Expose
    var savingtarget: Int,


    @SerializedName("email")
    @Expose
    var email: String
) {
    override fun toString(): String {
        return "LoginResponse(response_server=$response_server, errorMessage='$errorMessage', token='$token', pk=$pk, username='$username', first_name='$first_name', last_name='$last_name', location='$location', aadharcard='$aadharcard', age=$age, savingtarget=$savingtarget, email='$email')"
    }
}