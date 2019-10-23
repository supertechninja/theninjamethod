package com.mcwilliams.theninjamethod.ui.home;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0012B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0007\u001a\u00020\bH\u0016J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\bH\u0016J\u0018\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\bH\u0016J\u0014\u0010\u0011\u001a\u00020\n2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/mcwilliams/theninjamethod/ui/home/ExerciseListAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/mcwilliams/theninjamethod/ui/home/ExerciseListAdapter$ViewHolder;", "()V", "postList", "", "Lcom/mcwilliams/theninjamethod/model/Exercise;", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "updatePostList", "ViewHolder", "app_debug"})
public final class ExerciseListAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.mcwilliams.theninjamethod.ui.home.ExerciseListAdapter.ViewHolder> {
    private java.util.List<com.mcwilliams.theninjamethod.model.Exercise> postList;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.mcwilliams.theninjamethod.ui.home.ExerciseListAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.mcwilliams.theninjamethod.ui.home.ExerciseListAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    public final void updatePostList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.mcwilliams.theninjamethod.model.Exercise> postList) {
    }
    
    public ExerciseListAdapter() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/mcwilliams/theninjamethod/ui/home/ExerciseListAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/mcwilliams/theninjamethod/databinding/ExerciseItemBinding;", "(Lcom/mcwilliams/theninjamethod/databinding/ExerciseItemBinding;)V", "viewModel", "Lcom/mcwilliams/theninjamethod/viewmodel/ExerciseViewModel;", "bind", "", "post", "Lcom/mcwilliams/theninjamethod/model/Exercise;", "app_debug"})
    public static final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        private final com.mcwilliams.theninjamethod.viewmodel.ExerciseViewModel viewModel = null;
        private final com.mcwilliams.theninjamethod.databinding.ExerciseItemBinding binding = null;
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.mcwilliams.theninjamethod.model.Exercise post) {
        }
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        com.mcwilliams.theninjamethod.databinding.ExerciseItemBinding binding) {
            super(null);
        }
    }
}