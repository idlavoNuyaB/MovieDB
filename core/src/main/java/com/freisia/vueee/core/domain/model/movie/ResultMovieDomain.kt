package com.freisia.vueee.core.domain.model.movie

import com.google.gson.annotations.SerializedName

data class ResultMovieDomain (
    @SerializedName("page") val page: Int,
    @SerializedName("total_result") val totalResult: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val result: ArrayList<SearchMovieDomain>
)