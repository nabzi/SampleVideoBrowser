package ir.nabzi.samplevideobrowser

import android.app.Application
import com.vimeo.networking.Configuration
import com.vimeo.networking.VimeoClient
import ir.nabzi.samplevideobrowser.di.appModule
import ir.nabzi.samplevideobrowser.di.dbModule
import ir.nabzi.samplevideobrowser.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {
    override fun onCreate(){
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    appModule, networkModule, dbModule
                )
            )
        }
//        val configBuilder: Configuration.Builder = Configuration.Builder(
//            "49f96431580cf7798dcc0e7d65c9b1c9352e05de",
//            "Jwsp6XuZEk9HtG9ZLK81W7uso3mkCcaac+sZMH32VDGYBtWb/44cJysqobZtPcUhfIWvq4u3ODp8ApYg1xvoZVfajkeHxvJUbYUr2UDRVnVmFDvuTCUm//ZlL7Mi/04G",
//            "public"
//        )
//            .setCacheDirectory(this.cacheDir)
//        VimeoClient.initialize(configBuilder.build())
    }
}