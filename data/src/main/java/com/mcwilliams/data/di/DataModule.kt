package com.mcwilliams.data.di

import android.content.Context
import com.mcwilliams.data.ManualWorkoutsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    fun providesManualWorkoutRepository(@ApplicationContext context: Context): ManualWorkoutsRepository =
        ManualWorkoutsRepository(
            context
        )

}