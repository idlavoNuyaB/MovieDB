package com.freisia.vueee.core.data.model.tv

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RatingTVResponse (
    @SerializedName("results") val results : ArrayList<ResultsRatingTVResponse>,
    @SerializedName("id") val id : Int
) : Parcelable