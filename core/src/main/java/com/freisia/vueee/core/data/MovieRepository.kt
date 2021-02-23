package com.freisia.vueee.core.data

import com.freisia.vueee.core.data.local.source.MovieLocalDataSource
import com.freisia.vueee.core.data.remote.ApiResponse
import com.freisia.vueee.core.data.remote.source.MovieRemoteDataSource
import com.freisia.vueee.core.domain.model.movie.MovieDomain
import com.freisia.vueee.core.domain.model.movie.ResultMovieDomain
import com.freisia.vueee.core.domain.repository.IRepository
import com.freisia.vueee.core.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(
    private val remoteDataSource: MovieRemoteDataSource,
    private val localDataSource: MovieLocalDataSource,
) : IRepository<ApiResponse<ResultMovieDomain>, MovieDomain> {

    override suspend fun getPopularRemoteData(page: Int): Flow<ApiResponse<ResultMovieDomain>> {
        return flow {
            remoteDataSource.getList(page).collect {
                when(it){
                    is ApiResponse.Success -> {
                        val data = it.data
                        if (!data.result.isNullOrEmpty()) {
                            emit(ApiResponse.Success(DataMapper.mapResultMovieResponseToDomain(data)))
                        }
                    }
                    is ApiResponse.Empty -> {
                        emit( ApiResponse.Empty )
                    }
                    is ApiResponse.Error -> {
                        emit(ApiResponse.Error("Error"))
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getNowPlayingRemoteData(page: Int): Flow<ApiResponse<ResultMovieDomain>> {
        return flow {
            remoteDataSource.getNowPlayingList(page).collect {
                when(it){
                    is ApiResponse.Success -> {
                        val data = it.data
                        if (!data.result.isNullOrEmpty()) {
                            emit(ApiResponse.Success(DataMapper.mapResultMovieResponseToDomain(data)))
                        }
                    }
                    is ApiResponse.Empty -> {
                        emit( ApiResponse.Empty )
                    }
                    is ApiResponse.Error -> {
                        emit(ApiResponse.Error("Error"))
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getTopRatedRemoteData(page: Int): Flow<ApiResponse<ResultMovieDomain>> {
        return flow {
            remoteDataSource.getTopList(page).collect {
                when(it){
                    is ApiResponse.Success -> {
                        val data = it.data
                        if (!data.result.isNullOrEmpty()) {
                            emit(ApiResponse.Success(DataMapper.mapResultMovieResponseToDomain(data)))
                        }
                    }
                    is ApiResponse.Empty -> {
                        emit( ApiResponse.Empty )
                    }
                    is ApiResponse.Error -> {
                        emit(ApiResponse.Error("Error"))
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getDetailRemoteData(id:Int): Flow<ApiResponse<MovieDomain>> {
        return flow{
            remoteDataSource.getDetail(id).collect{
                when(it){
                    is ApiResponse.Success -> {
                        val data = it.data
                        emit(ApiResponse.Success(DataMapper.mapMovieRemoteResponseToDomain(data)))
                    }
                    is ApiResponse.Empty -> {
                        emit( ApiResponse.Empty )
                    }
                    is ApiResponse.Error -> {
                        emit(ApiResponse.Error("Error"))
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getFavoriteLocalData(): Flow<List<MovieDomain>> {
        return flow{
            localDataSource.getDatas().collect{
                emit(DataMapper.mapMovieLocalResponseToDomain(it))
            }
        }
    }

    override fun getListDetailLocalData(): Flow<List<MovieDomain>> {
        return flow{
            localDataSource.getDataDetails().collect {
                emit(DataMapper.mapMovieLocalResponseToDomain(it))
            }
        }
    }

    override suspend fun insertFavoriteLocalData(movie : MovieDomain) {
        localDataSource.insert(DataMapper.mapMovieRemoteDomainToResponse(movie))
    }

    override suspend fun deleteFavoriteLocalData(movie : MovieDomain){
        localDataSource.delete(DataMapper.mapMovieRemoteDomainToResponse(movie))
    }

    override suspend fun deleteAllLocalData(){
        localDataSource.deleteAll()
    }
}