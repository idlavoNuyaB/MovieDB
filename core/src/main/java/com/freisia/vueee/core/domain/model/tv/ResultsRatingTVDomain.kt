package com.freisia.vueee.core.domain.model.tv

import com.google.gson.annotations.SerializedName

data class ResultsRatingTVDomain (
    @SerializedName("iso_3166_1") val iso_3166_1 : String,
    @SerializedName("rating") val rating : String
)