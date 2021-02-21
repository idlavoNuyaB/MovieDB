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
import java.lang.ref.WeakReference

val viewmodelModule = module {
    single{MoviesViewModel(get())}
    single{TVShowsViewModel(get())}
    viewModel{(type: String) ->
        if(type == "movies" || type == "localmovies"){
            DetailViewModel<MovieUseCase, Movie>(get(), WeakReference(androidContext()) )
        } else {
            DetailViewModel<TVUseCase, TV>(get(),WeakReference(androidContext()))
        }
    }

}