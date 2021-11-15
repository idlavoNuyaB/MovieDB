package com.freisia.vueee.core.domain.model.tv

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RatingTVDomain (
    @SerializedName("results") val results : ArrayList<ResultsRatingTVDomain>,
    @SerializedName("id") val id : Int
) : Parcelable