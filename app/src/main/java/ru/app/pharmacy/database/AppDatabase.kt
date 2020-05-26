package ru.app.pharmacy.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.app.pharmacy.models.MedicineCart
import ru.app.pharmacy.models.MedicineDao

@Database(entities = [MedicineCart::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val medicineDao: MedicineDao
}