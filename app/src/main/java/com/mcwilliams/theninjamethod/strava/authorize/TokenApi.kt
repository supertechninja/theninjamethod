package com.mcwilliams.theninjamethod.strava.authorize

import com.mcwilliams.theninjamethod.strava.authorize.model.LoginResult
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TokenApi {
    @POST("/oauth/token")
    @FormUrlEncoded
    suspend fun token(
        @Field("client_id") clientID: Int,
        @Field("client_secret") clientSecret: String?,
        @Field("code") code: String?
    ): LoginResult?
}