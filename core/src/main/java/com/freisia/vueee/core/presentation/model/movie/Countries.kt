package com.freisia.vueee.core.presentation.model.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Countries (
    @SerializedName("certification") val certification : String,
    @SerializedName("iso_3166_1") val iso_3166_1 : String
) : Parcelable