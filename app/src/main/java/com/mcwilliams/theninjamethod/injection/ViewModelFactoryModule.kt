package com.mcwilliams.theninjamethod.injection

import androidx.lifecycle.ViewModelProvider
import com.mcwilliams.theninjamethod.utils.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}