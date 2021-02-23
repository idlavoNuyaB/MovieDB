package com.freisia.vueee.core.utils

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.freisia.vueee.core.data.remote.APIService
import com.freisia.vueee.core.data.remote.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class RemoteDataSourceFactory<T> (private val apiService: APIService,private val scope: CoroutineScope,private var state : Flow<ApiResponse<T>>) : DataSource.Factory<Int,T>() {
    private val liveData = MutableLiveData<RemoteDataSource<T>>()

    override fun create(): DataSource<Int, T> {
        val source = RemoteDataSource(apiService,scope,state)
        liveData.postValue(source)
        return source
    }
}