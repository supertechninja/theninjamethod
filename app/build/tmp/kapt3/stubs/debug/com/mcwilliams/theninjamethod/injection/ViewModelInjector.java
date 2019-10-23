package com.mcwilliams.theninjamethod.injection;

import java.lang.System;

/**
 * Component providing inject() methods for presenters.
 */
@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001:\u0001\u0006J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0007"}, d2 = {"Lcom/mcwilliams/theninjamethod/injection/ViewModelInjector;", "", "inject", "", "postListViewModel", "Lcom/mcwilliams/theninjamethod/viewmodel/ExerciseListViewModel;", "Builder", "app_debug"})
@dagger.Component(modules = {com.mcwilliams.theninjamethod.network.NetworkModule.class})
@javax.inject.Singleton()
public abstract interface ViewModelInjector {
    
    /**
     * Injects required dependencies into the specified PostListViewModel.
     * @param postListViewModel PostListViewModel in which to inject the dependencies
     */
    public abstract void inject(@org.jetbrains.annotations.NotNull()
    com.mcwilliams.theninjamethod.viewmodel.ExerciseListViewModel postListViewModel);
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\u0010\u0010\u0004\u001a\u00020\u00002\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/mcwilliams/theninjamethod/injection/ViewModelInjector$Builder;", "", "build", "Lcom/mcwilliams/theninjamethod/injection/ViewModelInjector;", "networkModule", "Lcom/mcwilliams/theninjamethod/network/NetworkModule;", "app_debug"})
    @dagger.Component.Builder()
    public static abstract interface Builder {
        
        @org.jetbrains.annotations.NotNull()
        public abstract com.mcwilliams.theninjamethod.injection.ViewModelInjector build();
        
        @org.jetbrains.annotations.NotNull()
        public abstract com.mcwilliams.theninjamethod.injection.ViewModelInjector.Builder networkModule(@org.jetbrains.annotations.NotNull()
        com.mcwilliams.theninjamethod.network.NetworkModule networkModule);
    }
}