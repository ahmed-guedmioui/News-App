package com.ag_apps.newsapp

import android.app.Application
import com.ag_apps.newsapp.core.di.coreModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * @author Ahmed Guedmioui
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                coreModule
            )
        }
    }

}