package com.freisia.vueee.core.data.model.movie

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.freisia.vueee.core.data.model.all.GenresResponse

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.RawValue

@Entity(tableName = "Movie")
data class MovieResponse (
    @ColumnInfo(name="genres") @SerializedName("genres") val genres : @RawValue ArrayList<GenresResponse>,
    @PrimaryKey @NonNull @ColumnInfo(name="id") @SerializedName("id") val id : Int,
    @ColumnInfo(name="overview") @SerializedName("overview") val overview : String,
    @ColumnInfo(name="poster_path") @SerializedName("poster_path") val poster_path : String?,
    @ColumnInfo(name="release_date") @SerializedName("release_date") val release_date : String,
    @ColumnInfo(name="runtime") @SerializedName("runtime") val runtime : Int,
    @ColumnInfo(name="title") @SerializedName("title") val title : String,
    @ColumnInfo(name="vote_average") @SerializedName("vote_average") val vote_average : Double,
    @ColumnInfo(name="vote_count") @SerializedName("vote_count") val vote_count : Int,
    @ColumnInfo(name="releases") @SerializedName("releases") val releases : @RawValue ReleasesResponse
)