package ru.app.apteka.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.app.apteka.models.MedicineCart
import ru.app.apteka.models.MedicineDao

@Database(entities = [MedicineCart::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val medicineDao: MedicineDao
}