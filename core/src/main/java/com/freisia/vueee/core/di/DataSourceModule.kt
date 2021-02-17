package com.freisia.vueee.core.di

import com.freisia.vueee.core.data.local.source.MovieLocalDataSource
import com.freisia.vueee.core.data.local.source.TVLocalDataSource
import com.freisia.vueee.core.data.remote.source.MovieRemoteDataSource
import com.freisia.vueee.core.data.remote.source.TVRemoteDataSource
import org.koin.dsl.module

val dataSourceModule = module{
    single{ TVRemoteDataSource(get()) }
    single{ MovieRemoteDataSource(get()) }
    single{ MovieLocalDataSource(get()) }
    single{ TVLocalDataSource(get()) }
}