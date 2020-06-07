package ru.app.pharmacy.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import ru.app.pharmacy.utils.Converter

@Entity(tableName = "cart")
@TypeConverters(Converter::class)
data class MedicineCart(
    @PrimaryKey val id: Long,
    val title: String,
    val image: String,
    val price: Float,
    var count: MutableLiveData<Int>,
    val categoryId: Long,
    val categoryName: String,
    val rating: Float,
    val available: Boolean,
    val description: String,
    var balance: Int
) {
    fun priceString(): String = String.format("%.2f ₽", price)
    fun toMedicine(): Medicine {
        val res = Medicine(
            id,
            title,
            categoryId,
            categoryName,
            image,
            price,
            rating,
            available,
            description,
            balance
        )
        res.count = count
        return res
    }
}

@Dao
interface MedicineDao {
    @Query("select * from cart")
    fun getAll(): LiveData<List<MedicineCart>>

    @Query("select * from cart")
    fun getAllList(): List<MedicineCart>

    @Query("select count(*) from cart")
    fun getCount(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(medicine: MedicineCart)

    @Update
    fun update(medicine: MedicineCart)

    @Delete
    fun delete(medicine: MedicineCart)

    @Query("delete from cart")
    fun deleteAll()

    @Query("update cart set balance = :balance where id = :id")
    fun updateItem(id: Long, balance: Int)
}