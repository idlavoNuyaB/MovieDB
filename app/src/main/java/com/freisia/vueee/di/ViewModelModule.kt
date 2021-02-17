package com.freisia.vueee.di

import com.freisia.vueee.core.domain.usecase.MovieUseCase
import com.freisia.vueee.core.domain.usecase.TVUseCase
import com.freisia.vueee.core.presentation.model.movie.Movie
import com.freisia.vueee.core.presentation.model.tv.TV
import com.freisia.vueee.presentation.detail.DetailViewModel
import com.freisia.vueee.presentation.list.movies.MoviesViewModel
import com.freisia.vueee.presentation.list.tvshow.TVShowsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewmodelModule = module {
    viewModel{MoviesViewModel(get())}
    viewModel{TVShowsViewModel(get())}
    viewModel{(type: String) ->
        if(type == "movies" || type == "localmovies"){
            DetailViewModel<MovieUseCase, Movie>(get(),androidContext())
        } else {
            DetailViewModel<TVUseCase, TV>(get(),androidContext())
        }
    }

}