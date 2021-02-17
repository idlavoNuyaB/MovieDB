package com.freisia.vueee.core.utils

import androidx.room.TypeConverter
import com.freisia.vueee.core.data.model.all.GenresResponse
import com.freisia.vueee.core.data.model.movie.ReleasesResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {
    @TypeConverter
    fun fromGenreToString(value: ArrayList<GenresResponse>) : String{
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun fromStringToGenre(value: String): ArrayList<GenresResponse>{
        val listType = object : TypeToken<ArrayList<GenresResponse>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromReleasetoString(value: ReleasesResponse): String{
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun fromStringToRelease(value: String): ReleasesResponse {
        val listType = object : TypeToken<ReleasesResponse>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromIntToString(value: ArrayList<Int>) : String{
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun fromStringToInt(value: String): ArrayList<Int>{
        val listType = object : TypeToken<ArrayList<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }
}