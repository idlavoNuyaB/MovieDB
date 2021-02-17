package com.freisia.vueee.favorite.di

import com.freisia.vueee.favorite.movies.MoviesFavoriteViewModel
import com.freisia.vueee.favorite.tvshow.TVShowsFavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteModule = module {
    viewModel{ MoviesFavoriteViewModel(get()) }
    viewModel{ TVShowsFavoriteViewModel(get()) }
}