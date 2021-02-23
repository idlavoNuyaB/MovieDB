package com.freisia.vueee.core.domain.usecase

import androidx.paging.DataSource
import com.freisia.vueee.core.data.MovieRepository
import com.freisia.vueee.core.data.remote.ApiResponse
import com.freisia.vueee.core.presentation.model.movie.Movie
import com.freisia.vueee.core.presentation.model.movie.SearchMovie
import com.freisia.vueee.core.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieUseCase(private val repository: MovieRepository) :
    IUseCase<ApiResponse<DataSource.Factory<Int,SearchMovie>?>, Movie> {
    override suspend fun getPopularRemoteData(page: Int): Flow<ApiResponse<DataSource.Factory<Int,SearchMovie>?>> {
        return flow {
            repository.getPopularRemoteData(page).collect { apiResponse ->
                when(apiResponse){
                    is ApiResponse.Success -> {
                        val data = apiResponse.data?.map {
                            DataMapper.mapSearchMovieDomainToPresentation(it)
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

    override suspend fun getNowPlayingRemoteData(page: Int): Flow<ApiResponse<DataSource.Factory<Int,SearchMovie>?>> {
//        return flow {
//            repository.getNowPlayingRemoteData(page).collect {
//                when(it){
//                    is ApiResponse.Success -> {
//                        val data = it.data
//                        if (!data.result.isNullOrEmpty()) {
//                            emit(ApiResponse.Success(DataMapper.mapResultMovieDomainToPresentation(data)))
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

    override suspend fun getTopRatedRemoteData(page: Int): Flow<ApiResponse<DataSource.Factory<Int,SearchMovie>?>> {
//        return flow {
//            repository.getTopRatedRemoteData(page).collect {
//                when(it){
//                    is ApiResponse.Success -> {
//                        val data = it.data
//                        if (!data.result.isNullOrEmpty()) {
//                            emit(ApiResponse.Success(DataMapper.mapResultMovieDomainToPresentation(data)))
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

    override suspend fun getDetailRemoteData(id :Int): Flow<ApiResponse<Movie>> {
        return flow{
            repository.getDetailRemoteData(id).collect{
                when(it){
                    is ApiResponse.Success -> {
                        val data = it.data
                        emit(ApiResponse.Success(DataMapper.mapMovieRemoteDomainToPresentation(data)))
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

    override fun getFavoriteLocalData(): Flow<List<Movie>> {
        return flow{
            repository.getFavoriteLocalData().collect{
                emit(DataMapper.mapMovieLocalDomainToPresentation(it))
            }
        }
    }

    override fun getListDetailLocalData(): Flow<List<Movie>> {
        return flow{
            repository.getListDetailLocalData().collect {
                emit(DataMapper.mapMovieLocalDomainToPresentation(it))
            }
        }
    }

    override suspend fun insertFavoriteLocalData(movie: Movie) {
        repository.insertFavoriteLocalData(DataMapper.mapMovieRemotePresentationToDomain(movie))
    }

    override suspend fun deleteFavoriteLocalData(movie: Movie) {
        repository.deleteFavoriteLocalData(DataMapper.mapMovieRemotePresentationToDomain(movie))
    }

    override suspend fun deleteAllLocalData() {
        repository.deleteAllLocalData()
    }

    override suspend fun getSearchRemoteData(
        page: Int,
        query: String
    ): Flow<ApiResponse<DataSource.Factory<Int,SearchMovie>?>> {
//        return flow{
//            repository.getSearchRemoteData(page, query).collect {
//                when(it){
//                    is ApiResponse.Success -> {
//                        val data = it.data
//                        emit(ApiResponse.Success(DataMapper.mapResultMovieDomainToPresentation(data)))
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