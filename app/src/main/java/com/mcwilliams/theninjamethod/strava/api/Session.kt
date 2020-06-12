package com.mcwilliams.theninjamethod.strava.api

import com.mcwilliams.theninjamethod.ui.settings.data.TokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface Session {
    @POST("/oauth/token")
    @FormUrlEncoded
    suspend fun getFirstToken(
        @Field("client_id") clientID: Int,
        @Field("client_secret") clientSecret: String?,
        @Field("code") code: String?,
        @Field("grant_type") grantType: String
    ): TokenResponse

    @POST("/oauth/token")
    @FormUrlEncoded
    suspend fun refreshToken(
        @Field("client_id") clientID: Int,
        @Field("client_secret") clientSecret: String?,
        @Field("refresh_token") refreshToken: String?,
        @Field("grant_type") grantType: String
    ): TokenResponse

}