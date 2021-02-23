package com.freisia.vueee.core.domain.usecase

import com.freisia.vueee.core.data.remote.ApiResponse
import kotlinx.coroutines.flow.Flow

interface IUseCase<T,Z> {
    suspend fun getPopularRemoteData(page: Int): Flow<T>
    suspend fun getNowPlayingRemoteData(page: Int): Flow<T>
    suspend fun getTopRatedRemoteData(page: Int): Flow<T>
    suspend fun getDetailRemoteData(id :Int): Flow<ApiResponse<Z>>
    fun getFavoriteLocalData(): Flow<List<Z>>
    fun getListDetailLocalData(): Flow<List<Z>>
    suspend fun insertFavoriteLocalData(movie : Z)
    suspend fun deleteFavoriteLocalData(movie : Z)
    suspend fun deleteAllLocalData()
}