package com.freisia.vueee.core.domain.model.all

import com.google.gson.annotations.SerializedName

data class GenresDomain (
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
)