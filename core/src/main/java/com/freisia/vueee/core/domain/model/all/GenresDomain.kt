package com.freisia.vueee.core.domain.model.all

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenresDomain (
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
) : Parcelable