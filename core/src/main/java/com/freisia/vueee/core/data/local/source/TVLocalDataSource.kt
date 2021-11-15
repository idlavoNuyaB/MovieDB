package com.freisia.vueee.core.data.local.source

import androidx.lifecycle.asFlow
import com.freisia.vueee.core.data.local.MovieDBDatabase
import com.freisia.vueee.core.data.model.tv.TVResponse
import com.freisia.vueee.core.utils.EspressoIdlingResource
import kotlinx.coroutines.flow.Flow

class TVLocalDataSource(private val movieDBDatabase: MovieDBDatabase) {

    fun getDatas() : Flow<List<TVResponse>>{
        EspressoIdlingResource.increment()
        val data = movieDBDatabase.tvDao().findAllTV()
        if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
            EspressoIdlingResource.decrement()
        }
        return data.asFlow()
    }

    fun getDataDetail() : Flow<List<TVResponse>> {
        EspressoIdlingResource.increment()
        val data = movieDBDatabase.tvDao().findAllTV()
        if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
            EspressoIdlingResource.decrement()
        }
        return data.asFlow()
    }

    suspend fun insert(tv: TVResponse) : Long {
        EspressoIdlingResource.increment()
        val data = movieDBDatabase.tvDao().insert(tv)
        if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
            EspressoIdlingResource.decrement()
        }
        return data
    }
    suspend fun delete(tv: TVResponse) {
        EspressoIdlingResource.increment()
        movieDBDatabase.tvDao().delete(tv)
        if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
            EspressoIdlingResource.decrement()
        }
    }
    suspend fun deleteAll() {
        EspressoIdlingResource.increment()
        movieDBDatabase.tvDao().deleteAll()
        if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
            EspressoIdlingResource.decrement()
        }
    }
}