package ru.app.apteka.koin

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.app.apteka.repositories.manager.SharedPrefsManager

val storageModule = module {
    single { SharedPrefsManager(androidContext()) }
}