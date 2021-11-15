package com.freisia.vueee.core.domain.model.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReleasesDomain (
    @SerializedName("countries") val countries : ArrayList<CountriesDomain>
): Parcelable