package com.freisia.vueee.core.data.model.tv

import com.google.gson.annotations.SerializedName

data class ResultsRatingTVResponse (
    @SerializedName("iso_3166_1") val iso_3166_1 : String,
    @SerializedName("rating") val rating : String
)