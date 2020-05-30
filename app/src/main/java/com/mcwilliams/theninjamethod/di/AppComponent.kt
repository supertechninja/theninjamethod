package com.mcwilliams.theninjamethod.di

import android.content.Context
import com.mcwilliams.theninjamethod.TheNinjaMethodApplication
import com.mcwilliams.theninjamethod.di.modules.ExercisesModule
import com.mcwilliams.theninjamethod.di.modules.SettingsModule
import com.mcwilliams.theninjamethod.di.modules.WorkoutsModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ExercisesModule::class,
    WorkoutsModule::class,
    SettingsModule::class,
    AndroidInjectionModule::class])
interface AppComponent : AndroidInjector<TheNinjaMethodApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }
}