package ru.app.pharmacy.koin

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.app.pharmacy.repositories.manager.SharedPrefsManager

val storageModule = module {
    single { SharedPrefsManager(androidContext()) }
}