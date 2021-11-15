package com.freisia.vueee.core.data.model.all

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenresResponse (
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
) : Parcelable