package com.freisia.vueee.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.freisia.vueee.core.data.local.dao.MovieDao
import com.freisia.vueee.core.data.local.dao.TVDao
import com.freisia.vueee.core.data.model.movie.MovieResponse
import com.freisia.vueee.core.data.model.tv.TVResponse
import com.freisia.vueee.core.utils.Converter

@Database(entities = [MovieResponse::class, TVResponse::class],version = 1,exportSchema = false)
@TypeConverters(Converter::class)
abstract class MovieDBDatabase : RoomDatabase(){
    abstract fun movieDao(): MovieDao
    abstract fun tvDao(): TVDao
}