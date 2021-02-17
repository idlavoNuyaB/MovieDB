package com.freisia.vueee.core.presentation.model.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Releases (
    @SerializedName("countries") val countries : ArrayList<Countries>
): Parcelable