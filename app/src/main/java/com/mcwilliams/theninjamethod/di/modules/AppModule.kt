package com.mcwilliams.theninjamethod.di.modules

import android.content.Context
import com.mcwilliams.theninjamethod.network.apis.ExerciseApi
import com.mcwilliams.theninjamethod.strava.ISessionRepository
import com.mcwilliams.theninjamethod.strava.SessionRepository
import com.mcwilliams.theninjamethod.strava.api.AthleteApi
import com.mcwilliams.theninjamethod.strava.api.Session
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.IStravaWorkoutRepository
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.StravaWorkoutRepository
import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.IManualWorkoutsRepository
import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.ManualWorkoutsRepository
import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db.WorkoutDao
import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.db.WorkoutDatabase
import com.mcwilliams.theninjamethod.ui.activity.stravadetail.IStravaWorkoutDetailRepository
import com.mcwilliams.theninjamethod.ui.activity.stravadetail.StravaWorkoutDetailRepository
import com.mcwilliams.theninjamethod.ui.exercises.db.ExerciseDao
import com.mcwilliams.theninjamethod.ui.exercises.db.ExerciseDatabase
import com.mcwilliams.theninjamethod.ui.exercises.repository.ExerciseRepository
import com.mcwilliams.theninjamethod.ui.exercises.repository.IExerciseRepository
import com.mcwilliams.theninjamethod.ui.routines.IRoutinesRepository
import com.mcwilliams.theninjamethod.ui.routines.RoutinesRepository
import com.mcwilliams.theninjamethod.ui.routines.db.RoutineDao
import com.mcwilliams.theninjamethod.ui.routines.db.RoutinesDatabase
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
    fun provideSettingsRepository(
        settingsRepoImpl: SessionRepository,
        athleteApi: AthleteApi
    ): SettingsRepo =
        SettingsRepoImpl(settingsRepoImpl, athleteApi)

    @Provides
    @Singleton
    fun provideWorkoutRepository(
        @ApplicationContext context: Context,
        athleteApi: AthleteApi
    ): IStravaWorkoutRepository =
        StravaWorkoutRepository(
            context,
            athleteApi
        )

    @Provides
    @Singleton
    fun provideStravaDetailRepository(athleteApi: AthleteApi): IStravaWorkoutDetailRepository =
        StravaWorkoutDetailRepository(
            athleteApi
        )

    @Provides
    @Singleton
    fun providesSessionRepository(
        @ApplicationContext context: Context,
        session: Session
    ): ISessionRepository =
        SessionRepository(context, session)

    @Provides
    @Singleton
    fun providesManualWorkoutRepository(@ApplicationContext context: Context): IManualWorkoutsRepository =
        ManualWorkoutsRepository(
            context
        )

    @Provides
    @Singleton
    fun providesRoutinesRepository(@ApplicationContext context: Context): IRoutinesRepository =
        RoutinesRepository(
            context
        )

    @Provides
    @Singleton
    fun providesExerciseRepository(
        @ApplicationContext context: Context,
        exerciseApi: ExerciseApi
    ): IExerciseRepository =
        ExerciseRepository(context, exerciseApi)


    @Provides
    @Singleton
    fun provideExerciseDB(@ApplicationContext context: Context): ExerciseDatabase =
        ExerciseDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideWorkoutDB(@ApplicationContext context: Context): WorkoutDatabase =
        WorkoutDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideRoutineDB(@ApplicationContext context: Context): RoutinesDatabase =
        RoutinesDatabase.getDatabase(context)


    @Provides
    @Singleton
    fun provideExerciseDao(db: ExerciseDatabase): ExerciseDao = db.exerciseDao()

    @Provides
    @Singleton
    fun provideWorkoutDao(db: WorkoutDatabase): WorkoutDao = db.workoutDao()

    @Provides
    @Singleton
    fun provideRoutineDao(db: RoutinesDatabase): RoutineDao = db.routineDao()

}