package com.mcwilliams.theninjamethod.injection

import android.content.Context
import com.mcwilliams.theninjamethod.TheNinjaMethodApplication
import com.mcwilliams.theninjamethod.injection.modules.ExercisesModule
import com.mcwilliams.theninjamethod.injection.modules.SettingsModule
import com.mcwilliams.theninjamethod.injection.modules.WorkoutsModule
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