package ru.app.apteka.koin

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.app.apteka.database.AppDatabase
import ru.app.apteka.models.MedicineDao
import kotlin.math.sin

val databaseModule = module {

    fun provideDatabase(application: Application): AppDatabase =
        Room.databaseBuilder(application, AppDatabase::class.java, "appdb")
            .build()

    fun provideMedicineDao(database: AppDatabase): MedicineDao = database.medicineDao

    single { provideDatabase(androidApplication()) }
    single { provideMedicineDao(get()) }
}