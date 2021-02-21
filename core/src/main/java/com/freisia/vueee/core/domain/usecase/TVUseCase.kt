package com.freisia.vueee.core.domain.usecase

import com.freisia.vueee.core.data.TVRepository
import com.freisia.vueee.core.data.remote.ApiResponse
import com.freisia.vueee.core.presentation.model.tv.RatingTV
import com.freisia.vueee.core.presentation.model.tv.ResultTV
import com.freisia.vueee.core.presentation.model.tv.TV
import com.freisia.vueee.core.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TVUseCase (private val repository: TVRepository) : IUseCase<ApiResponse<ResultTV>, TV>,
    IRatingTVUseCase {
    override suspend fun getPopularRemoteData(page: Int): Flow<ApiResponse<ResultTV>> {
        return flow {
            repository.getPopularRemoteData(page).collect {
                when(it){
                    is ApiResponse.Success -> {
                        val data = it.data
                        if (!data.result.isNullOrEmpty()) {
                            emit(ApiResponse.Success(DataMapper.mapResultTVDomainToPresentation(data)))
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

    override suspend fun getNowPlayingRemoteData(page: Int): Flow<ApiResponse<ResultTV>> {
        return flow {
            repository.getNowPlayingRemoteData(page).collect {
                when(it){
                    is ApiResponse.Success -> {
                        val data = it.data
                        if (!data.result.isNullOrEmpty()) {
                            emit(ApiResponse.Success(DataMapper.mapResultTVDomainToPresentation(data)))
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

    override suspend fun getTopRatedRemoteData(page: Int): Flow<ApiResponse<ResultTV>> {
        return flow {
            repository.getTopRatedRemoteData(page).collect {
                when(it){
                    is ApiResponse.Success -> {
                        val data = it.data
                        if (!data.result.isNullOrEmpty()) {
                            emit(ApiResponse.Success(DataMapper.mapResultTVDomainToPresentation(data)))
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

    override suspend fun getDetailRemoteData(id :Int): Flow<ApiResponse<TV>> {
        return flow{
            repository.getDetailRemoteData(id).collect{
                when(it){
                    is ApiResponse.Success -> {
                        val data = it.data
                        emit(ApiResponse.Success(DataMapper.mapTVDomainToPresentation(data)))
                    }
                    is ApiResponse.Empty -> {
                        emit( ApiResponse.Empty )
                    }
                    is ApiResponse.Error -> {
                        emit(ApiResponse.Error("Error"))
                    }
                }
            }
        }.flowOn(Dispatchers.IO)    }

    override fun getFavoriteLocalData(): Flow<List<TV>> {
        return flow{
            repository.getFavoriteLocalData().collect {
                emit(DataMapper.mapTVLocalDomainToPresentation(it))
            }
        }
    }

    override fun getListDetailLocalData(): Flow<List<TV>> {
        return flow{
            repository.getListDetailLocalData().collect {
                emit(DataMapper.mapTVLocalDomainToPresentation(it))
            }
        }
    }

    override suspend fun insertFavoriteLocalData(movie: TV) {
        repository.insertFavoriteLocalData(DataMapper.mapTVPresentationToDomain(movie))
    }

    override suspend fun deleteFavoriteLocalData(movie: TV) {
        repository.deleteFavoriteLocalData(DataMapper.mapTVPresentationToDomain(movie))
    }

    override suspend fun deleteAllLocalData() {
        repository.deleteAllLocalData()
    }

    override suspend fun getRatingTV(id: Int): Flow<ApiResponse<RatingTV>> {
        return flow {
            repository.getRatingTV(id).collect {
                when(it){
                    is ApiResponse.Success -> {
                        val data = it.data
                        emit(ApiResponse.Success(DataMapper.mapRatingTVDomainToPresentation(data)))
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

    override suspend fun getSearchRemoteData(
        page: Int,
        query: String
    ): Flow<ApiResponse<ResultTV>> {
        return flow{
            repository.getSearchRemoteData(page, query).collect {
                when(it){
                    is ApiResponse.Success -> {
                        val data = it.data
                        emit(ApiResponse.Success(DataMapper.mapResultTVDomainToPresentation(data)))
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
}
