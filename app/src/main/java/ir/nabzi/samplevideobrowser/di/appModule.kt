package ir.nabzi.samplevideobrowser.di

import com.google.gson.Gson
import ir.nabzi.samplevideobrowser.data.repository.ContentDBDataSource
import ir.nabzi.samplevideobrowser.data.repository.ContentRemoteDataSource
import ir.nabzi.samplevideobrowser.data.repository.ContentRepository
import ir.nabzi.samplevideobrowser.data.repository.ContentRepositoryImpl
import ir.nabzi.samplevideobrowser.ui.home.ContentViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single                  { Gson()                            }

    //Repository
    single<ContentRepository> { ContentRepositoryImpl(get(), get()) }


    //DataSource
    single                  { ContentRemoteDataSource(get())      }
    single                  { ContentDBDataSource(get())          }

    //Viewmodel
    viewModel               { ContentViewModel(get() )     }

}