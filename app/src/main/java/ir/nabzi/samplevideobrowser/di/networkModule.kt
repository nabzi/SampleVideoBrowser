package ir.nabzi.samplevideobrowser.di

import ir.nabzi.samplevideobrowser.data.remote.BASE_URL
import ir.nabzi.samplevideobrowser.data.remote.createHttpClient
import ir.nabzi.samplevideobrowser.data.remote.createRetrofit
import ir.nabzi.samplevideobrowser.data.remote.createService

import org.koin.dsl.module

val networkModule = module {
    single { createService(get()) }
    single { createHttpClient() }
    single { createRetrofit(get(), BASE_URL) }
}

