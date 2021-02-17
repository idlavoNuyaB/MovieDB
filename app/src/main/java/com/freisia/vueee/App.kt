package com.freisia.vueee

import android.app.Application
import com.freisia.vueee.core.di.*
import com.freisia.vueee.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@App)
            modules(networkModule)
            modules(localModule(this@App))
            modules(dataSourceModule)
            modules(viewmodelModule)
            modules(repositoryModule)
            modules(useCaseModule)
        }
    }
}