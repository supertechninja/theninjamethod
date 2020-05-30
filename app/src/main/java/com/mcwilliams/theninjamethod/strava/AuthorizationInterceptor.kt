package com.mcwilliams.theninjamethod.strava

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthorizationInterceptor(private val token: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header("Accept", "application/json")
            .header("Authorization", token)
            .method(original.method(), original.body())
        val request = requestBuilder.build()
        return chain.proceed(request)
    }

}