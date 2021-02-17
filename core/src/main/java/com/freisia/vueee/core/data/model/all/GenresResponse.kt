package com.freisia.vueee.core.data.model.all

import com.google.gson.annotations.SerializedName

data class GenresResponse (
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
)