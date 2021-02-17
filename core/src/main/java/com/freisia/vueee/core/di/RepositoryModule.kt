package com.freisia.vueee.core.di


import com.freisia.vueee.core.data.MovieRepository
import com.freisia.vueee.core.data.TVRepository
import org.koin.dsl.module

val repositoryModule = module{
    single{ MovieRepository(get(),get()) }
    single{ TVRepository(get(),get()) }
}