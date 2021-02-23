package com.freisia.vueee.core.data.model.tv

import com.google.gson.annotations.SerializedName

data class SearchTVResponse (
    @SerializedName("name") val name : String,
    @SerializedName("id") val id : Int,
    @SerializedName("vote_average") val vote_average : Double,
    @SerializedName("poster_path") val poster_path : String?
)