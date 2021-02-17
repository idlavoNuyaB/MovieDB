package com.freisia.vueee.core.data.model.movie

import com.google.gson.annotations.SerializedName

data class CountriesResponse (
    @SerializedName("certification") val certification : String,
    @SerializedName("iso_3166_1") val iso_3166_1 : String,
    @SerializedName("primary") val primary : Boolean,
    @SerializedName("release_date") val release_date : String
)