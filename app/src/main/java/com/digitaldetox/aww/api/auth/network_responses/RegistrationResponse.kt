package com.digitaldetox.aww.api.auth.network_responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegistrationResponse(

    @SerializedName("response")
    @Expose
    var response_registration_server: String,

    @SerializedName("error_message")
    @Expose
    var errorMessage: String,

    @SerializedName("email")
    @Expose
    var email: String,

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

    @SerializedName("pk")
    @Expose
    var pk: Int,

    @SerializedName("token")
    @Expose
    var token: String)
{

    override fun toString(): String {
        return "RegistrationResponse(response_registration_server='$response_registration_server', errorMessage='$errorMessage', email='$email', username='$username', first_name='$first_name', last_name='$last_name', location='$location', aadharcard='$aadharcard', age=$age, savingtarget=$savingtarget, pk=$pk, token='$token')"
    }
}