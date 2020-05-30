package com.mcwilliams.theninjamethod.network

import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.ui.settings.data.TokenResponse
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
    ): TokenResponse
}