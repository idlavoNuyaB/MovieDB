package com.freisia.vueee.core.di

import android.app.Application
import androidx.room.Room
import com.freisia.vueee.core.data.local.MovieDBDatabase
import com.freisia.vueee.core.utils.Constant
import com.freisia.vueee.core.utils.Hashing
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.core.module.Module
import org.koin.dsl.module

fun localModule(application: Application) : Module{
    return module {
        single {
            val phrase = Constant.GET_PASSPHRASE
            val encryptPhrase = Hashing.getSha(phrase)
            val passphrase: ByteArray = SQLiteDatabase.getBytes(encryptPhrase.toCharArray())
            val factory = SupportFactory(passphrase)
            Room.databaseBuilder(
                application.applicationContext,
                MovieDBDatabase::class.java,
                "moviedb.db"
            ).fallbackToDestructiveMigration()
                .openHelperFactory(factory)
                .build()
        }
    }
}