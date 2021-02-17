package com.freisia.vueee.core.domain.model.movie

import com.google.gson.annotations.SerializedName

data class ReleasesDomain (
    @SerializedName("countries") val countries : ArrayList<CountriesDomain>
)