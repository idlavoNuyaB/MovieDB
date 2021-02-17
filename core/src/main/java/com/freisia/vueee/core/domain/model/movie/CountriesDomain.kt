package com.freisia.vueee.core.domain.model.movie

import com.google.gson.annotations.SerializedName

data class CountriesDomain (
    @SerializedName("certification") val certification : String,
    @SerializedName("iso_3166_1") val iso_3166_1 : String,
    @SerializedName("primary") val primary : Boolean,
    @SerializedName("release_date") val release_date : String
)