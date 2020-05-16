package ru.app.apteka.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import ru.app.apteka.utils.Converter

@Entity(tableName = "cart")
@TypeConverters(Converter::class)
data class MedicineCart(
    @PrimaryKey val id:Long,
    val title:String,
    val image:String,
    val price:Float,
    var count:MutableLiveData<Int>
)
{
    fun priceString():String = String.format("%.2f ₽", price)
}

@Dao
interface MedicineDao{
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
}