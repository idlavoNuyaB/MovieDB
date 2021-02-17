package com.freisia.vueee.core.domain.model.tv

import com.freisia.vueee.core.domain.model.all.GenresDomain
import com.google.gson.annotations.SerializedName

data class TVDomain (
    @SerializedName("episode_run_time") val episode_run_time : ArrayList<Int>,
    @SerializedName("first_air_date") val first_air_date : String,
    @SerializedName("genres") val genres : ArrayList<GenresDomain>,
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("overview") val overview : String,
    @SerializedName("poster_path") val poster_path : String,
    @SerializedName("vote_average") val vote_average : Double,
    @SerializedName("vote_count") val vote_count : Int
)