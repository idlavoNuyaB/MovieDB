package com.freisia.vueee.core.utils

import androidx.paging.PageKeyedDataSource
import com.freisia.vueee.core.data.remote.APIService
import com.freisia.vueee.core.data.remote.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class RemoteDataSource<T> (private val apiService: APIService,private val scope: CoroutineScope,private var state : Flow<ApiResponse<T>>) : PageKeyedDataSource<Int,T>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        try {
            scope.launch {
                val api = apiService.getMovies(1)
                if(api.isSuccessful){
                    val data = api.body()
                    if(data?.result.isNullOrEmpty()){
                        state = flow {
                            ApiResponse.Empty
                        }
                    } else {
                        state = flow {
                            ApiResponse.NotEmpty
                        }
                        callback.onResult(data?.result as List<T>,null,2)
                    }
                } else{
                    state = flow {
                        ApiResponse.Error("Connection Error")
                    }
                }

            }
        } catch(e : Exception){
            state = flow {
                ApiResponse.Error(e.toString())
            }
        }

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        try {
            scope.launch {
                val api = apiService.getMovies(params.key)
                if(api.isSuccessful){
                    val data = api.body()
                    if(data?.result.isNullOrEmpty()){
                        state = flow {
                            ApiResponse.Empty
                        }
                    } else {
                        state = flow {
                            ApiResponse.NotEmpty
                        }
                        callback.onResult(data?.result as List<T>,params.key + 1)
                    }
                } else{
                    state = flow {
                        ApiResponse.Error("Connection Error")
                    }
                }

            }
        } catch(e : Exception){
            state = flow {
                ApiResponse.Error(e.toString())
            }
        }

    }
}