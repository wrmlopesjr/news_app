package dev.wilsonjr.newsapp

import android.app.Application
import dev.wilsonjr.faire.base.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NewsApplication)
            modules(appComponent)
        }
    }
}