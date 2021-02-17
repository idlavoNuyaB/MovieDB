package com.freisia.vueee.core.domain.usecase

import com.freisia.vueee.core.data.remote.ApiResponse
import com.freisia.vueee.core.presentation.model.tv.RatingTV
import kotlinx.coroutines.flow.Flow

interface IRatingTVUseCase {
    suspend fun getRatingTV(id:Int) : Flow<ApiResponse<RatingTV>>

}