package com.mcwilliams.theninjamethod.di.modules

import com.mcwilliams.theninjamethod.network.apis.ExerciseApi
import com.mcwilliams.theninjamethod.strava.api.Session
import com.mcwilliams.theninjamethod.network.apis.WorkoutApi
import com.mcwilliams.theninjamethod.strava.AuthorizationInterceptor
import com.mcwilliams.theninjamethod.strava.TokenAuthenticator
import com.mcwilliams.theninjamethod.strava.api.AthleteApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named


/**
 * Module which provides all required dependencies about network
 */
@InstallIn(ApplicationComponent::class)
@Module
@Suppress("unused")
object NetworkModule {
    /**
     * Provides the Post service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the Post service implementation.
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideExerciseApi(@Named("sheety") retrofit: Retrofit): ExerciseApi {
        return retrofit.create(ExerciseApi::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideWorkoutApi(@Named("sheety") retrofit: Retrofit): WorkoutApi {
        return retrofit.create(WorkoutApi::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideStravaSession(@Named("strava") retrofit: Retrofit): Session {
        return retrofit.create(Session::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideAthlete(@Named("stravaApi") retrofit: Retrofit): AthleteApi {
        return retrofit.create(AthleteApi::class.java)
    }

    /**
     * a strava api makes the calls to the api and attaches the token to the header with an okhttp interceptor from the session. Session should have a
     * getter method that checks the expiration and automatically gets a new token if needed. Session
     */

    @Provides
    @Named("stravaApi")
    @Reusable
    @JvmStatic
    internal fun provideStravaApi(
        okHttpClient: OkHttpClient.Builder,
        authenticator: TokenAuthenticator,
        authorizationInterceptor: AuthorizationInterceptor
    ): Retrofit {
        okHttpClient.addInterceptor(authorizationInterceptor)
        okHttpClient.authenticator(authenticator)

        return Retrofit.Builder()
            .baseUrl("https://www.strava.com/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .build()
    }


    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */
    @Provides
    @Named("sheety")
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(okHttpClient: OkHttpClient.Builder): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://v2-api.sheety.co/3914cbe7336242c6f95cabe092d3ae4e/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(okHttpClient.build())
            .build()
    }

    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */
    @Provides
    @Named("strava")
    @Reusable
    @JvmStatic
    internal fun provideStravaRetrofitInterface(okHttpClient: OkHttpClient.Builder): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.strava.com/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .build()
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideOkHttp(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(logging)
        return okHttpClient
    }
}