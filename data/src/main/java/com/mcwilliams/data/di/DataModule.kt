package com.mcwilliams.data.di

import android.content.Context
import com.mcwilliams.data.ManualWorkoutsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun providesManualWorkoutRepository(@ApplicationContext context: Context): ManualWorkoutsRepository =
        ManualWorkoutsRepository(
            context
        )

}