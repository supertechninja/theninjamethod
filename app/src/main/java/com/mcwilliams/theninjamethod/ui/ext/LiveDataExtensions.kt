package com.mcwilliams.theninjamethod.ui.ext

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

//Transforms an observable into a livedata
fun <T, U> Observable<T>.toLiveData(
    disposable: CompositeDisposable,
    transform: (T) -> U
): LiveData<U> {
    return MutableLiveData<U>().also { liveData ->
        disposable.add(this.subscribeOn(Schedulers.io()).subscribe { nextValue ->
            liveData.postValue(transform(nextValue))
        })
    }
}