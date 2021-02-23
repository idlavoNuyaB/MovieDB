package com.freisia.vueee.core.utils

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

class LocalDataSourceFactory<T> (private val data: List<T>) : DataSource.Factory<Int,T>() {
    private val liveData = MutableLiveData<LocalDataSource<T>>()

    override fun create(): DataSource<Int, T> {
        val source = LocalDataSource(data)
        liveData.postValue(source)
        return source
    }

}