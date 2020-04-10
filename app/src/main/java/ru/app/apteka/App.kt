package ru.app.apteka

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.app.apteka.koin.appComponent
import ru.app.apteka.koin.repositoryModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@App)
            modules(appComponent)
        }
    }
}