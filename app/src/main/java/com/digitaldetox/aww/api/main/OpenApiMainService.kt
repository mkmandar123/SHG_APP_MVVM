package com.digitaldetox.aww.api.main

import com.digitaldetox.aww.api.GenericResponse
import com.digitaldetox.aww.api.main.responses.*
import com.digitaldetox.aww.di.main.MainScope
import com.digitaldetox.aww.models.AccountProperties
import retrofit2.http.*

@MainScope
interface OpenApiMainService {


    @GET("users/properties/")
    suspend fun getAccountProperties(
        @Header("Authorization") authorization: String
    ): AccountProperties

    @PUT("users/properties/update")
    @FormUrlEncoded
    suspend fun saveAccountProperties(
        @Header("Authorization") authorization: String,
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("location") location: String,
        @Field("age") age: Int,
        @Field("savingtarget") savingtarget: Int

    ): GenericResponse

    @PUT("account/change_password/")
    @FormUrlEncoded
    suspend fun updatePassword(
        @Header("Authorization") authorization: String,
        @Field("old_password") currentPassword: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_new_password") confirmNewPassword: String
    ): GenericResponse

    @GET("subreddits/user-subreddit-list/{username}/")
    suspend fun searchListSubredditSubscribedByUser(
        @Path("username") username: String,
        @Query("search") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ): SubredditListSearchResponse

    @GET("users/profile/{username}/")
    suspend fun searchListHumanloanprofileSubscribedByUser(
        @Path("username") username: String
    ): HumanloanprofileListSearchResponse

    @GET("users/profile/{username}/")
    suspend fun searchListHumansavingprofileSubscribedByUser(
        @Path("username") username: String
    ): HumansavingprofileListSearchResponse



    @GET("blog/{slug}/is_author")
    suspend fun isAuthorOfSubreddit(
        @Header("Authorization") authorization: String,
        @Path("slug") slug: String
    ): GenericResponse




    @DELETE("blog/{slug}/delete")
    suspend fun deleteSubreddit(
        @Header("Authorization") authorization: String,
        @Path("slug") slug: String
    ): GenericResponse

    @FormUrlEncoded
    @PATCH("subreddits/sub/{subreddit_name}/")
    suspend fun updateSubreddit(
        @Header("Authorization") authorization: String,
        @Path("subreddit_name") subreddit_name: String,
        @Field("title") title: String,
        @Field("description") description: String
    ): SubredditCreateUpdateResponse


    @GET("loanrequests/user-loanrequest-list/{authorsender}/")
    suspend fun searchListUserloanrequest(
        @Path("authorsender") authorsender: String,
        @Query("search") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ): UserloanrequestListSearchResponse

    @DELETE("root/{slug}/delete")
    suspend fun deleteUserloanrequest(
        @Header("Authorization") authorization: String,
        @Path("slug") slug: String
    ): GenericResponse

    @FormUrlEncoded
    @PATCH("loanrequests/{loanrequest_pk}/")
    suspend fun updateUserloanrequest(
        @Header("Authorization") authorization: String,
        @Path("loanrequest_pk") loanrequest_pk: String,
        @Field("title") title: String,
        @Field("body") body: String,
        @Field("loanamount") loanamount: Int
    ): UserloanrequestCreateUpdateResponse



    @GET("savingrequests/user-savingrequest-list/{authorsender}/")
    suspend fun searchListUsersavingrequest(
        @Path("authorsender") authorsender: String,
        @Query("search") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ): UsersavingrequestListSearchResponse

    @DELETE("root/{slug}/delete")
    suspend fun deleteUsersavingrequest(
        @Header("Authorization") authorization: String,
        @Path("slug") slug: String
    ): GenericResponse

    @FormUrlEncoded
    @PATCH("savingrequests/{savingrequest_pk}/")
    suspend fun updateUsersavingrequest(
        @Header("Authorization") authorization: String,
        @Path("savingrequest_pk") savingrequest_pk: String,
        @Field("title") title: String,
        @Field("body") body: String,
        @Field("savingamount") savingamount: Int

    ): UsersavingrequestCreateUpdateResponse

}