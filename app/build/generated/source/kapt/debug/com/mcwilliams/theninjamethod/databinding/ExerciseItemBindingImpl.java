package com.mcwilliams.theninjamethod.databinding;
import com.mcwilliams.theninjamethod.R;
import com.mcwilliams.theninjamethod.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class ExerciseItemBindingImpl extends ExerciseItemBinding  {

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
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ExerciseItemBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 3, sIncludes, sViewsWithIds));
    }
    private ExerciseItemBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 2
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[1]
            );
        this.exerciseBody.setTag(null);
        this.exerciseTitle.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8L;
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
        if (BR.exerciseViewModel == variableId) {
            setExerciseViewModel((com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setExerciseViewModel(@Nullable com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseViewModel ExerciseViewModel) {
        this.mExerciseViewModel = ExerciseViewModel;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.exerciseViewModel);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeExerciseViewModelGetExerciseTitle((androidx.lifecycle.MutableLiveData<java.lang.String>) object, fieldId);
            case 1 :
                return onChangeExerciseViewModelGetExerciseBody((androidx.lifecycle.MutableLiveData<java.lang.String>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeExerciseViewModelGetExerciseTitle(androidx.lifecycle.MutableLiveData<java.lang.String> ExerciseViewModelGetExerciseTitle, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeExerciseViewModelGetExerciseBody(androidx.lifecycle.MutableLiveData<java.lang.String> ExerciseViewModelGetExerciseBody, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
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
        java.lang.String exerciseViewModelGetExerciseTitleGetValue = null;
        com.mcwilliams.theninjamethod.ui.exercises.viewmodel.ExerciseViewModel exerciseViewModel = mExerciseViewModel;
        androidx.lifecycle.MutableLiveData<java.lang.String> exerciseViewModelGetExerciseTitle = null;
        androidx.lifecycle.MutableLiveData<java.lang.String> exerciseViewModelGetExerciseBody = null;
        java.lang.String exerciseViewModelGetExerciseBodyGetValue = null;

        if ((dirtyFlags & 0xfL) != 0) {


            if ((dirtyFlags & 0xdL) != 0) {

                    if (exerciseViewModel != null) {
                        // read exerciseViewModel.getExerciseTitle()
                        exerciseViewModelGetExerciseTitle = exerciseViewModel.getExerciseTitle();
                    }
                    updateLiveDataRegistration(0, exerciseViewModelGetExerciseTitle);


                    if (exerciseViewModelGetExerciseTitle != null) {
                        // read exerciseViewModel.getExerciseTitle().getValue()
                        exerciseViewModelGetExerciseTitleGetValue = exerciseViewModelGetExerciseTitle.getValue();
                    }
            }
            if ((dirtyFlags & 0xeL) != 0) {

                    if (exerciseViewModel != null) {
                        // read exerciseViewModel.getExerciseBody()
                        exerciseViewModelGetExerciseBody = exerciseViewModel.getExerciseBody();
                    }
                    updateLiveDataRegistration(1, exerciseViewModelGetExerciseBody);


                    if (exerciseViewModelGetExerciseBody != null) {
                        // read exerciseViewModel.getExerciseBody().getValue()
                        exerciseViewModelGetExerciseBodyGetValue = exerciseViewModelGetExerciseBody.getValue();
                    }
            }
        }
        // batch finished
        if ((dirtyFlags & 0xeL) != 0) {
            // api target 1

            com.mcwilliams.theninjamethod.utils.BindingAdaptersKt.setMutableText(this.exerciseBody, exerciseViewModelGetExerciseBody);
        }
        if ((dirtyFlags & 0xdL) != 0) {
            // api target 1

            com.mcwilliams.theninjamethod.utils.BindingAdaptersKt.setMutableText(this.exerciseTitle, exerciseViewModelGetExerciseTitle);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): exerciseViewModel.getExerciseTitle()
        flag 1 (0x2L): exerciseViewModel.getExerciseBody()
        flag 2 (0x3L): exerciseViewModel
        flag 3 (0x4L): null
    flag mapping end*/
    //end
}