package com.freisia.vueee.core.domain.repository

import com.freisia.vueee.core.data.remote.ApiResponse
import com.freisia.vueee.core.domain.model.tv.RatingTVDomain
import kotlinx.coroutines.flow.Flow

interface IRatingTVRepository {
    suspend fun getRatingTV(id:Int) : Flow<ApiResponse<RatingTVDomain>>
}