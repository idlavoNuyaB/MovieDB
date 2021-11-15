package com.freisia.vueee.core.data.local.source

import androidx.lifecycle.asFlow
import com.freisia.vueee.core.data.local.MovieDBDatabase
import com.freisia.vueee.core.data.model.movie.MovieResponse
import com.freisia.vueee.core.utils.EspressoIdlingResource
import kotlinx.coroutines.flow.Flow

class MovieLocalDataSource(private val movieDBDatabase: MovieDBDatabase) {

    fun getDatas() : Flow<List<MovieResponse>> {
        EspressoIdlingResource.increment()
        val res = movieDBDatabase.movieDao().findAllMovie()
        if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
            EspressoIdlingResource.decrement()
        }
        return res.asFlow()
    }

    fun getDataDetails() : Flow<List<MovieResponse>>
    {
        EspressoIdlingResource.increment()
        val res = movieDBDatabase.movieDao().findAllMovie()
        if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
            EspressoIdlingResource.decrement()
        }
        return res.asFlow()
    }

    suspend fun insert(movie: MovieResponse) : Long {
        EspressoIdlingResource.increment()
        val res = movieDBDatabase.movieDao().insert(movie)
        if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
            EspressoIdlingResource.decrement()
        }
        return res
    }

    suspend fun delete(movie: MovieResponse) {
        EspressoIdlingResource.increment()
        movieDBDatabase.movieDao().delete(movie)
        if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
            EspressoIdlingResource.decrement()
        }
    }

    suspend fun deleteAll() {
        EspressoIdlingResource.increment()
        movieDBDatabase.movieDao().deleteAll()
        if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
            EspressoIdlingResource.decrement()
        }
    }
}