package com.mcwilliams.theninjamethod.di.modules

import android.content.Context
import com.mcwilliams.theninjamethod.strava.SessionRepository
import com.mcwilliams.theninjamethod.strava.api.AthleteApi
import com.mcwilliams.theninjamethod.strava.api.Session
import com.mcwilliams.theninjamethod.ui.settings.repo.SettingsRepo
import com.mcwilliams.theninjamethod.ui.settings.repo.SettingsRepoImpl
import com.mcwilliams.theninjamethod.ui.workouts.WorkoutRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module(includes = [NetworkModule::class])
class AppModule {

    @Provides
    fun provideSettingsRepository(settingsRepoImpl: SessionRepository, athleteApi: AthleteApi) : SettingsRepo =
        SettingsRepoImpl(settingsRepoImpl, athleteApi)

    @Provides
    fun provideWorkoutRepository(athleteApi: AthleteApi) : WorkoutRepo =
        WorkoutRepo(athleteApi)

    @Provides
    @Singleton
    fun providesSessionRepository(@ApplicationContext context: Context, session: Session) : SessionRepository =
        SessionRepository(context, session)

}