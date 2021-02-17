package com.freisia.vueee.core.di

import android.app.Application
import androidx.room.Room
import com.freisia.vueee.core.data.local.MovieDBDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

fun localModule(application: Application) : Module{
    return module {
        single {
            Room.databaseBuilder(
                application.applicationContext,
                MovieDBDatabase::class.java,
                "moviedb.db"
            ).fallbackToDestructiveMigration().build()
        }
    }
}