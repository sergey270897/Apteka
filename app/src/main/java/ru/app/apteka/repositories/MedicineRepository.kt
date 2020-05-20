package ru.app.apteka.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import retrofit2.await
import ru.app.apteka.models.Medicine
import ru.app.apteka.models.MedicineCart
import ru.app.apteka.models.MedicineDao
import ru.app.apteka.network.AptekaAPI

class MedicineRepository(private val aptekaAPI: AptekaAPI, private val medicineDao: MedicineDao) {

    suspend fun getMedicine(
        q: String = "",
        categoryId: Int = 0,
        priceFrom: Int = 0,
        priceTo: Int = 20000,
        available: Boolean = false,
        count: Int = 20,
        offset: Int = 0,
        orderBy: String = "price",
        order: String = "asc"
    ): List<Medicine> {
        val res = aptekaAPI.searchMedicine(
            q,
            categoryId,
            priceFrom,
            priceTo,
            available,
            count,
            offset,
            orderBy,
            order
        ).await()
        return res.products
    }

    fun getCartCount(): LiveData<Int> = medicineDao.getCount()

    fun getCartItems(): LiveData<List<MedicineCart>> = medicineDao.getAll()

    fun getCartItemsList(): List<MedicineCart> = medicineDao.getAllList()

    suspend fun addCartItem(medicineCart: MedicineCart) = medicineDao.add(medicineCart)

    suspend fun updateCartItem(medicineCart: MedicineCart) = medicineDao.update(medicineCart)

    suspend fun deleteCartItem(medicineCart: MedicineCart) = medicineDao.delete(medicineCart)
}