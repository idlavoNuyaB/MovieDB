package com.freisia.vueee.favorite.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.freisia.vueee.core.domain.usecase.MovieUseCase
import com.freisia.vueee.core.presentation.model.movie.Movie
import com.freisia.vueee.core.utils.LocalDataSourceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MoviesFavoriteViewModel(private val useCase: MovieUseCase) : ViewModel() {

    lateinit var data : LiveData<PagedList<Movie>>

    fun getData() = viewModelScope.launch(Dispatchers.IO){
        useCase.getFavoriteLocalData().collect {
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(20)
                .setPageSize(20)
                .setPrefetchDistance(5)
                .build()
            val dataSourceFactory = LocalDataSourceFactory(it)
            data = LivePagedListBuilder(dataSourceFactory,config).build()

        }
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        useCase.deleteAllLocalData()
    }
}
