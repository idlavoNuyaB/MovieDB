package com.freisia.vueee.core.presentation.model.movie

import android.os.Parcelable
import com.freisia.vueee.core.presentation.model.all.Genres
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie (
    @SerializedName("genres") val genres : ArrayList<Genres>,
    @SerializedName("id") val id : Int,
    @SerializedName("overview") val overview : String,
    @SerializedName("poster_path") val poster_path : String?,
    @SerializedName("release_date") val release_date : String,
    @SerializedName("runtime") val runtime : Int,
    @SerializedName("title") val title : String,
    @SerializedName("vote_average") val vote_average : Double,
    @SerializedName("vote_count") val vote_count : Int,
    @SerializedName("releases") val releases : Releases
) : Parcelable