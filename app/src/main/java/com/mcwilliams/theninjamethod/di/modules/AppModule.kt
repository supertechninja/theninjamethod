package com.mcwilliams.theninjamethod.di.modules

import android.content.Context
import com.mcwilliams.appinf.Session
import com.mcwilliams.appinf.SessionRepository
import com.mcwilliams.theninjamethod.network.apis.ExerciseApi
import com.mcwilliams.theninjamethod.strava.api.AthleteApi
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.StravaWorkoutRepository
import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.ManualWorkoutsRepository
import com.mcwilliams.theninjamethod.ui.activity.stravadetail.StravaWorkoutDetailRepository
import com.mcwilliams.theninjamethod.ui.exercises.repository.ExerciseRepository
import com.mcwilliams.theninjamethod.ui.routines.RoutinesRepository
import com.mcwilliams.theninjamethod.ui.settings.repo.SettingsRepo
import com.mcwilliams.theninjamethod.ui.settings.repo.SettingsRepoImpl
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
    @Singleton
    fun provideSettingsRepository(settingsRepoImpl: SessionRepository, athleteApi: AthleteApi) : SettingsRepo =
        SettingsRepoImpl(settingsRepoImpl, athleteApi)

    @Provides
    @Singleton
    fun provideWorkoutRepository(
        @ApplicationContext context: Context,
        athleteApi: AthleteApi
    ): StravaWorkoutRepository =
        StravaWorkoutRepository(
            context,
            athleteApi
        )

    @Provides
    @Singleton
    fun provideStravaDetailRepository(athleteApi: AthleteApi): StravaWorkoutDetailRepository =
        StravaWorkoutDetailRepository(
            athleteApi
        )

    @Provides
    @Singleton
    fun providesSessionRepository(
        @ApplicationContext context: Context,
        session: Session
    ): SessionRepository =
        SessionRepository(context, session)

    @Provides
    @Singleton
    fun providesManualWorkoutRepository(@ApplicationContext context: Context): ManualWorkoutsRepository =
        ManualWorkoutsRepository(
            context
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