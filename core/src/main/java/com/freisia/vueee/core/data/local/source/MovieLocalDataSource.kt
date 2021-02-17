package com.freisia.vueee.core.data.local.source

import androidx.lifecycle.asFlow
import com.freisia.vueee.core.data.local.MovieDBDatabase
import com.freisia.vueee.core.data.model.movie.MovieResponse
import kotlinx.coroutines.flow.Flow

class MovieLocalDataSource(private val movieDBDatabase: MovieDBDatabase) {

    fun getDatas() : Flow<List<MovieResponse>> {
        val res = movieDBDatabase.movieDao().findAllMovie()
        return res.asFlow()
    }

    fun getDataDetails() : Flow<List<MovieResponse>>
    {
        val res = movieDBDatabase.movieDao().findAllMovie()
        return res.asFlow()
    }

    suspend fun insert(movie: MovieResponse) : Long {
        return movieDBDatabase.movieDao().insert(movie)
    }

    suspend fun delete(movie: MovieResponse) {
        movieDBDatabase.movieDao().delete(movie)
    }

    suspend fun deleteAll() {
        movieDBDatabase.movieDao().deleteAll()
    }
}