package com.mcwilliams.theninjamethod.network;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J\u000e\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H\'\u00a8\u0006\t"}, d2 = {"Lcom/mcwilliams/theninjamethod/network/ExerciseApi;", "", "addExercise", "Lio/reactivex/Observable;", "Lcom/mcwilliams/theninjamethod/model/Exercise;", "exercise", "Lcom/mcwilliams/theninjamethod/model/AddExerciseRequest;", "getExercises", "Lcom/mcwilliams/theninjamethod/model/Data;", "app_debug"})
public abstract interface ExerciseApi {
    
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.GET(value = "exercisesApi/exercises")
    public abstract io.reactivex.Observable<com.mcwilliams.theninjamethod.model.Data> getExercises();
    
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.POST(value = "exercisesApi/exercises")
    public abstract io.reactivex.Observable<com.mcwilliams.theninjamethod.model.Exercise> addExercise(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Body()
    com.mcwilliams.theninjamethod.model.AddExerciseRequest exercise);
}