package com.freisia.vueee.core.data.model.movie

import com.google.gson.annotations.SerializedName

data class SearchMovieResponse (
    @SerializedName("poster_path") val poster_path : String?,
    @SerializedName("id") val id : Int,
    @SerializedName("backdrop_path") val backdrop_path : String?,
    @SerializedName("title") val title : String,
    @SerializedName("vote_average") val vote_average : Double,
)