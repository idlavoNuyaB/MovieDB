package com.freisia.vueee.core.data.model.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReleasesResponse (
    @SerializedName("countries") val countries : ArrayList<CountriesResponse>
): Parcelable