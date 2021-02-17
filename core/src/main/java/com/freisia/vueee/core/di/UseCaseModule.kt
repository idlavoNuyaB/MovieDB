package com.freisia.vueee.core.di

import com.freisia.vueee.core.domain.usecase.MovieUseCase
import com.freisia.vueee.core.domain.usecase.TVUseCase
import org.koin.dsl.module

val useCaseModule = module{
    single{ MovieUseCase(get()) }
    single{ TVUseCase(get()) }
}