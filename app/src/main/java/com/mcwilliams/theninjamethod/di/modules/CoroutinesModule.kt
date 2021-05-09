package com.mcwilliams.theninjamethod.di.modules

import com.mcwilliams.theninjamethod.di.DefaultDispatcher
import com.mcwilliams.theninjamethod.di.IoDispatcher
import com.mcwilliams.theninjamethod.di.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@InstallIn(ActivityComponent::class)
@Module
object CoroutinesModule {

    @DefaultDispatcher
    @JvmStatic
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @JvmStatic
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @JvmStatic
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}