package com.mcwilliams.settings.di

import com.mcwilliams.appinf.SessionRepository
import com.mcwilliams.settings.AthleteApi
import com.mcwilliams.settings.repo.SettingsRepo
import com.mcwilliams.settings.repo.SettingsRepoImpl
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Module which provides all required dependencies about network
 */
@InstallIn(ApplicationComponent::class)
@Module
@Suppress("unused")
object SettingsModule {

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideAthlete(@Named("stravaApi") retrofit: Retrofit): AthleteApi {
        return retrofit.create(AthleteApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(
        settingsRepoImpl: SessionRepository,
        athleteApi: AthleteApi
    ): SettingsRepo =
        SettingsRepoImpl(settingsRepoImpl, athleteApi)

}