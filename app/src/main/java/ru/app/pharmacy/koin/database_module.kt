package ru.app.pharmacy.koin

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.app.pharmacy.database.AppDatabase
import ru.app.pharmacy.models.MedicineDao

val databaseModule = module {

    fun provideDatabase(application: Application): AppDatabase =
        Room.databaseBuilder(application, AppDatabase::class.java, "appDB")
            .build()

    fun provideMedicineDao(database: AppDatabase): MedicineDao = database.medicineDao

    single { provideDatabase(androidApplication()) }
    single { provideMedicineDao(get()) }
}