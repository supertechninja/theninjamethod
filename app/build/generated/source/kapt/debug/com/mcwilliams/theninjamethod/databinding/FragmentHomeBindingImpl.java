package com.mcwilliams.theninjamethod.databinding;
import com.mcwilliams.theninjamethod.R;
import com.mcwilliams.theninjamethod.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class FragmentHomeBindingImpl extends FragmentHomeBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @NonNull
    private final android.widget.ProgressBar mboundView1;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentHomeBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 3, sIncludes, sViewsWithIds));
    }
    private FragmentHomeBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (androidx.recyclerview.widget.RecyclerView) bindings[2]
            );
        this.exerciseList.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.ProgressBar) bindings[1];
        this.mboundView1.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.exerciseListViewModel == variableId) {
            setExerciseListViewModel((com.mcwilliams.theninjamethod.viewmodel.ExerciseListViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setExerciseListViewModel(@Nullable com.mcwilliams.theninjamethod.viewmodel.ExerciseListViewModel ExerciseListViewModel) {
        this.mExerciseListViewModel = ExerciseListViewModel;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.exerciseListViewModel);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeExerciseListViewModelGetLoadingVisibility((androidx.lifecycle.MutableLiveData<java.lang.Integer>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeExerciseListViewModelGetLoadingVisibility(androidx.lifecycle.MutableLiveData<java.lang.Integer> ExerciseListViewModelGetLoadingVisibility, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        com.mcwilliams.theninjamethod.ui.home.ExerciseListAdapter exerciseListViewModelGetExerciseListAdapter = null;
        androidx.lifecycle.MutableLiveData<java.lang.Integer> exerciseListViewModelGetLoadingVisibility = null;
        com.mcwilliams.theninjamethod.viewmodel.ExerciseListViewModel exerciseListViewModel = mExerciseListViewModel;
        java.lang.Integer exerciseListViewModelGetLoadingVisibilityGetValue = null;

        if ((dirtyFlags & 0x7L) != 0) {


            if ((dirtyFlags & 0x6L) != 0) {

                    if (exerciseListViewModel != null) {
                        // read exerciseListViewModel.getExerciseListAdapter()
                        exerciseListViewModelGetExerciseListAdapter = exerciseListViewModel.getExerciseListAdapter();
                    }
            }

                if (exerciseListViewModel != null) {
                    // read exerciseListViewModel.getLoadingVisibility()
                    exerciseListViewModelGetLoadingVisibility = exerciseListViewModel.getLoadingVisibility();
                }
                updateLiveDataRegistration(0, exerciseListViewModelGetLoadingVisibility);


                if (exerciseListViewModelGetLoadingVisibility != null) {
                    // read exerciseListViewModel.getLoadingVisibility().getValue()
                    exerciseListViewModelGetLoadingVisibilityGetValue = exerciseListViewModelGetLoadingVisibility.getValue();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x6L) != 0) {
            // api target 1

            com.mcwilliams.theninjamethod.utils.BindingAdaptersKt.setAdapter(this.exerciseList, exerciseListViewModelGetExerciseListAdapter);
        }
        if ((dirtyFlags & 0x7L) != 0) {
            // api target 1

            com.mcwilliams.theninjamethod.utils.BindingAdaptersKt.setMutableVisibility(this.mboundView1, exerciseListViewModelGetLoadingVisibility);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): exerciseListViewModel.getLoadingVisibility()
        flag 1 (0x2L): exerciseListViewModel
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}