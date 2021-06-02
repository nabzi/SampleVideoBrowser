package ir.nabzi.aroundme.di

import com.google.gson.Gson
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single                  { Gson()                            }

    //Repository
//    single<VideoRepository> { VideoRepositoryImpl(get(), get()) }

    //DataSource
//    single                  { VideoRemoteDataSource(get())      }

    //Viewmodel
//    viewModel               { HomeViewModel(get() , get() )     }

}