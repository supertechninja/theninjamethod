package com.mcwilliams.theninjamethod.injection.modules

import androidx.lifecycle.ViewModel
import com.mcwilliams.theninjamethod.injection.ViewModelFactoryModule
import com.mcwilliams.theninjamethod.injection.ViewModelKey
import com.mcwilliams.theninjamethod.network.NetworkModule
import com.mcwilliams.theninjamethod.ui.settings.SettingsFragment
import com.mcwilliams.theninjamethod.ui.settings.SettingsViewModel
import com.mcwilliams.theninjamethod.ui.settings.repo.SettingsRepo
import com.mcwilliams.theninjamethod.ui.settings.repo.SettingsRepoImpl
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module(includes = [ViewModelFactoryModule::class, NetworkModule::class])
abstract class SettingsModule {

    @ContributesAndroidInjector
    internal abstract fun contributesSettingsFragment(): SettingsFragment

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(settingsRepoImpl: SettingsRepoImpl) : SettingsRepo
}