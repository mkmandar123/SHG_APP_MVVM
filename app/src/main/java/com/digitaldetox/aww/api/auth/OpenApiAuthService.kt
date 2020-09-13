package com.digitaldetox.aww.api.auth

import com.digitaldetox.aww.api.auth.network_responses.LoginResponse
import com.digitaldetox.aww.api.auth.network_responses.RegistrationResponse
import com.digitaldetox.aww.di.auth.AuthScope
import retrofit2.http.*

@AuthScope
interface OpenApiAuthService {

    @POST("users/login/")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("users/create/")
    @FormUrlEncoded
    suspend fun register(
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("location") location: String,
        @Field("aadharcard") aadharcard: String,
        @Field("age") age: Int,
        @Field("savingtarget") savingtarget: Int,
        @Field("password") password: String
    ): RegistrationResponse

}