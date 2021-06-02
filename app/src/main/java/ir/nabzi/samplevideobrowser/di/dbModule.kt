package ir.nabzi.samplevideobrowser.di

import android.app.Application
import androidx.room.Room
import ir.nabzi.samplevideobrowser.data.db.ContentDao
import ir.nabzi.samplevideobrowser.data.db.DB
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dbModule = module {
    fun provideDatabase(application: Application): DB {
        return Room.databaseBuilder(application, DB::class.java, "contents").build()
    }

    fun provideContentDao(database: DB): ContentDao {
        return database.contentDao()
    }

    single { provideDatabase(androidApplication()) }

    single { provideContentDao(get()) }
}