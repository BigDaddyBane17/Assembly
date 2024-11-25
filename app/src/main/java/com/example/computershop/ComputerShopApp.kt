package com.example.computershop

import android.app.Application
import com.example.computershop.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ComputerShopApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ComputerShopApp)
            androidLogger()
            modules(appModule)
        }
    }
}