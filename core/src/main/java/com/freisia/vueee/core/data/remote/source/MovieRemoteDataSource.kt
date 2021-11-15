package com.freisia.vueee.core.data.remote.source

import com.freisia.vueee.core.data.model.movie.MovieResponse
import com.freisia.vueee.core.data.model.movie.ResultMovieResponse
import com.freisia.vueee.core.data.remote.APIService
import com.freisia.vueee.core.data.remote.ApiResponse
import com.freisia.vueee.core.utils.EspressoIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRemoteDataSource(private val apiService: APIService){

    suspend fun getDetail(id: Int) : Flow<ApiResponse<MovieResponse>> {
        return flow {
            try {
                EspressoIdlingResource.increment()
                val api = apiService.getMovieDetail(id)
                if(api.isSuccessful){
                    emit(ApiResponse.Success(api.body() as MovieResponse))
                } else{
                    emit(ApiResponse.Empty)
                }
                if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
                    EspressoIdlingResource.decrement()
                }
            } catch(e : Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getList(page: Int) : Flow<ApiResponse<ResultMovieResponse>> {
        return flow {
            try {
                EspressoIdlingResource.increment()
                val api = apiService.getMovies(page)
                if(api.isSuccessful){
                    emit(ApiResponse.Success(api.body() as ResultMovieResponse))
                } else{
                    emit(ApiResponse.Empty)
                }
                if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
                    EspressoIdlingResource.decrement()
                }
            } catch(e : Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getNowPlayingList(page:Int) : Flow<ApiResponse<ResultMovieResponse>>{
        return flow {
            try {
                EspressoIdlingResource.increment()
                val api = apiService.getNowPlayingMovies(page)
                if(api.isSuccessful){
                    emit(ApiResponse.Success(api.body() as ResultMovieResponse))
                } else{
                    emit(ApiResponse.Empty)
                }
                if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
                    EspressoIdlingResource.decrement()
                }
            } catch(e : Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getTopList(page: Int) : Flow<ApiResponse<ResultMovieResponse>>{
        return flow {
            try {
                EspressoIdlingResource.increment()
                val api = apiService.getTopRatedMovies(page)
                if(api.isSuccessful){
                    emit(ApiResponse.Success(api.body() as ResultMovieResponse))
                } else{
                    emit(ApiResponse.Empty)
                }
                if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
                    EspressoIdlingResource.decrement()
                }
            } catch(e : Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}