package com.freisia.vueee.core.presentation.model.movie

import com.google.gson.annotations.SerializedName

data class ResultMovie (
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val result: ArrayList<SearchMovie>
)