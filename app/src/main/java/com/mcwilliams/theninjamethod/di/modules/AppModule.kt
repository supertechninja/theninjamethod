package com.mcwilliams.theninjamethod.di.modules

import android.content.Context
import com.mcwilliams.appinf.SessionRepository
import com.mcwilliams.appinf.di.StravaNetworkModule
import com.mcwilliams.data.di.DataModule
import com.mcwilliams.theninjamethod.network.apis.ExerciseApi
import com.mcwilliams.theninjamethod.strava.api.ActivitiesApi
import com.mcwilliams.theninjamethod.strava.api.AthleteApi
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.StravaWorkoutRepository
import com.mcwilliams.theninjamethod.ui.activity.stravadetail.StravaWorkoutDetailRepository
import com.mcwilliams.theninjamethod.ui.exercises.repository.ExerciseRepository
import com.mcwilliams.theninjamethod.ui.routines.RoutinesRepository
import com.mcwilliams.theninjamethod.ui.settings.SettingsRepo
import com.mcwilliams.theninjamethod.ui.settings.SettingsRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [NetworkModule::class, DataModule::class, StravaNetworkModule::class], )
class AppModule {

    @Provides
    @Singleton
    fun provideWorkoutRepository(
        @ApplicationContext context: Context,
        activitiesApi: ActivitiesApi
    ): StravaWorkoutRepository =
        StravaWorkoutRepository(
            context,
            activitiesApi
        )

    @Provides
    @Singleton
    fun provideSettingsRepository(
        settingsRepoImpl: SessionRepository,
        athleteApi: AthleteApi
    ): SettingsRepo =
        SettingsRepoImpl(settingsRepoImpl, athleteApi)

    @Provides
    @Singleton
    fun provideStravaDetailRepository(activitiesApi: ActivitiesApi): StravaWorkoutDetailRepository =
        StravaWorkoutDetailRepository(
            activitiesApi
        )

    @Provides
    @Singleton
    fun providesRoutinesRepository(@ApplicationContext context: Context): RoutinesRepository =
        RoutinesRepository(
            context
        )

    @Provides
    @Singleton
    fun providesExerciseRepository(
        @ApplicationContext context: Context,
        exerciseApi: ExerciseApi
    ): ExerciseRepository =
        ExerciseRepository(context, exerciseApi)
}