package com.freisia.vueee.core.data.model.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CountriesResponse (
    @SerializedName("certification") val certification : String,
    @SerializedName("iso_3166_1") val iso_3166_1 : String,
    @SerializedName("primary") val primary : Boolean,
    @SerializedName("release_date") val release_date : String
) : Parcelable