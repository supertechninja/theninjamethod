package com.mcwilliams.theninjamethod.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001a\u001a\u00020\u001bH\u0002J\b\u0010\u001c\u001a\u00020\u001bH\u0014J\b\u0010\u001d\u001a\u00020\u001bH\u0002J\b\u0010\u001e\u001a\u00020\u001bH\u0002J\b\u0010\u001f\u001a\u00020\u001bH\u0002J\u0016\u0010 \u001a\u00020\u001b2\f\u0010!\u001a\b\u0012\u0004\u0012\u00020#0\"H\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001e\u0010\f\u001a\u00020\r8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0012\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u000bR\u000e\u0010\u0018\u001a\u00020\u0019X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcom/mcwilliams/theninjamethod/viewmodel/ExerciseListViewModel;", "Lcom/mcwilliams/theninjamethod/viewmodel/BaseViewModel;", "()V", "errorClickListener", "Landroid/view/View$OnClickListener;", "getErrorClickListener", "()Landroid/view/View$OnClickListener;", "errorMessage", "Landroidx/lifecycle/MutableLiveData;", "", "getErrorMessage", "()Landroidx/lifecycle/MutableLiveData;", "exerciseApi", "Lcom/mcwilliams/theninjamethod/network/ExerciseApi;", "getExerciseApi", "()Lcom/mcwilliams/theninjamethod/network/ExerciseApi;", "setExerciseApi", "(Lcom/mcwilliams/theninjamethod/network/ExerciseApi;)V", "exerciseListAdapter", "Lcom/mcwilliams/theninjamethod/ui/home/ExerciseListAdapter;", "getExerciseListAdapter", "()Lcom/mcwilliams/theninjamethod/ui/home/ExerciseListAdapter;", "loadingVisibility", "getLoadingVisibility", "subscription", "Lio/reactivex/disposables/Disposable;", "loadExercises", "", "onCleared", "onRetrievePostListError", "onRetrievePostListFinish", "onRetrievePostListStart", "onRetrievePostListSuccess", "exerciseList", "", "Lcom/mcwilliams/theninjamethod/model/Exercise;", "app_debug"})
public final class ExerciseListViewModel extends com.mcwilliams.theninjamethod.viewmodel.BaseViewModel {
    @org.jetbrains.annotations.NotNull()
    @javax.inject.Inject()
    public com.mcwilliams.theninjamethod.network.ExerciseApi exerciseApi;
    private io.reactivex.disposables.Disposable subscription;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Integer> loadingVisibility = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Integer> errorMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final android.view.View.OnClickListener errorClickListener = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mcwilliams.theninjamethod.ui.home.ExerciseListAdapter exerciseListAdapter = null;
    
    @org.jetbrains.annotations.NotNull()
    public final com.mcwilliams.theninjamethod.network.ExerciseApi getExerciseApi() {
        return null;
    }
    
    public final void setExerciseApi(@org.jetbrains.annotations.NotNull()
    com.mcwilliams.theninjamethod.network.ExerciseApi p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Integer> getLoadingVisibility() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Integer> getErrorMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.view.View.OnClickListener getErrorClickListener() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mcwilliams.theninjamethod.ui.home.ExerciseListAdapter getExerciseListAdapter() {
        return null;
    }
    
    private final void loadExercises() {
    }
    
    private final void onRetrievePostListStart() {
    }
    
    private final void onRetrievePostListFinish() {
    }
    
    private final void onRetrievePostListSuccess(java.util.List<com.mcwilliams.theninjamethod.model.Exercise> exerciseList) {
    }
    
    private final void onRetrievePostListError() {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
    
    public ExerciseListViewModel() {
        super();
    }
}