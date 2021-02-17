package com.freisia.vueee.core.domain.model.tv

import com.google.gson.annotations.SerializedName

data class RatingTVDomain (
    @SerializedName("results") val results : ArrayList<ResultsRatingTVDomain>,
    @SerializedName("id") val id : Int
)