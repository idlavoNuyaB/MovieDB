package com.freisia.vueee.core.presentation.model.tv

import android.os.Parcelable
import com.freisia.vueee.core.presentation.model.all.Genres
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TV (
    @SerializedName("episode_run_time") val episode_run_time : ArrayList<Int>,
    @SerializedName("first_air_date") val first_air_date : String,
    @SerializedName("genres") val genres : ArrayList<Genres>,
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("overview") val overview : String,
    @SerializedName("poster_path") val poster_path : String?,
    @SerializedName("vote_average") val vote_average : Double,
    @SerializedName("vote_count") val vote_count : Int
) : Parcelable