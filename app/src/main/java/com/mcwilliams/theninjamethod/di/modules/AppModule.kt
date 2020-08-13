package com.mcwilliams.theninjamethod.di.modules

import android.content.Context
import com.mcwilliams.theninjamethod.network.apis.ExerciseApi
import com.mcwilliams.theninjamethod.strava.api.ActivitiesApi
import com.mcwilliams.theninjamethod.ui.activity.combinedworkoutlist.StravaWorkoutRepository
import com.mcwilliams.theninjamethod.ui.activity.manualworkoutdetail.ManualWorkoutsRepository
import com.mcwilliams.theninjamethod.ui.activity.stravadetail.StravaWorkoutDetailRepository
import com.mcwilliams.theninjamethod.ui.exercises.repository.ExerciseRepository
import com.mcwilliams.theninjamethod.ui.routines.RoutinesRepository
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
    fun provideStravaDetailRepository(activitiesApi: ActivitiesApi): StravaWorkoutDetailRepository =
        StravaWorkoutDetailRepository(
            activitiesApi
        )


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