package com.freisia.vueee.core.data

import androidx.paging.DataSource
import com.freisia.vueee.core.data.local.source.MovieLocalDataSource
import com.freisia.vueee.core.data.remote.ApiResponse
import com.freisia.vueee.core.data.remote.source.MovieRemoteDataSource
import com.freisia.vueee.core.domain.model.movie.MovieDomain
import com.freisia.vueee.core.domain.model.movie.SearchMovieDomain
import com.freisia.vueee.core.domain.repository.IRepository
import com.freisia.vueee.core.utils.DataMapper
import com.freisia.vueee.core.utils.RemoteDataSourceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(
    private val remoteDataSource: MovieRemoteDataSource,
    private val localDataSource: MovieLocalDataSource,
) : IRepository<ApiResponse<DataSource.Factory<Int,SearchMovieDomain>?>, MovieDomain> {

    override suspend fun getPopularRemoteData(page: Int): Flow<ApiResponse<DataSource.Factory<Int,SearchMovieDomain>?>> {
        return flow {
            remoteDataSource.getList(page).collect { apiResponse ->
                when(apiResponse){
                    is ApiResponse.Success -> {
                        val data = apiResponse.data?.map {
                            DataMapper.mapSearchMovieResponseToDomain(it)
                        }
                        emit(ApiResponse.Success(data))
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

    override suspend fun getNowPlayingRemoteData(page: Int): Flow<ApiResponse<DataSource.Factory<Int,SearchMovieDomain>?>> {
//        return flow {
//            remoteDataSource.getNowPlayingList(page).collect {
//                when(it){
//                    is ApiResponse.Success -> {
//                        val data = it.data
//                        if (!data.result.isNullOrEmpty()) {
//                            emit(ApiResponse.Success(DataMapper.mapResultMovieResponseToDomain(data)))
//                        }
//                    }
//                    is ApiResponse.Empty -> {
//                        emit( ApiResponse.Empty )
//                    }
//                    is ApiResponse.Error -> {
//                        emit(ApiResponse.Error("Error"))
//                    }
//                }
//            }
//        }.flowOn(Dispatchers.IO)
        TODO()
    }

    override suspend fun getTopRatedRemoteData(page: Int): Flow<ApiResponse<DataSource.Factory<Int,SearchMovieDomain>?>> {
//        return flow {
//            remoteDataSource.getTopList(page).collect {
//                when(it){
//                    is ApiResponse.Success -> {
//                        val data = it.data
//                        if (!data.result.isNullOrEmpty()) {
//                            emit(ApiResponse.Success(DataMapper.mapResultMovieResponseToDomain(data)))
//                        }
//                    }
//                    is ApiResponse.Empty -> {
//                        emit( ApiResponse.Empty )
//                    }
//                    is ApiResponse.Error -> {
//                        emit(ApiResponse.Error("Error"))
//                    }
//                }
//            }
//        }.flowOn(Dispatchers.IO)
        TODO()
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

    override suspend fun getSearchRemoteData(
        page: Int,
        query: String
    ): Flow<ApiResponse<DataSource.Factory<Int,SearchMovieDomain>?>> {
//        return flow{
//            remoteDataSource.getSearchList(page, query).collect {
//                when(it){
//                    is ApiResponse.Success -> {
//                        val data = it.data
//                        emit(ApiResponse.Success(DataMapper.mapResultMovieResponseToDomain(data)))
//                    }
//                    is ApiResponse.Empty -> {
//                        emit( ApiResponse.Empty )
//                    }
//                    is ApiResponse.Error -> {
//                        emit(ApiResponse.Error("Error"))
//                    }
//                }
//            }
//        }.flowOn(Dispatchers.IO)
        TODO()

    }

}