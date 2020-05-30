package com.mcwilliams.theninjamethod.utils

import com.mcwilliams.theninjamethod.network.Result
import io.reactivex.rxjava3.core.Observable


interface IRepository<T> {

    val data: Observable<Result<T>>

    fun refresh()

    fun clear()
}