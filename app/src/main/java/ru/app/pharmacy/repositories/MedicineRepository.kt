package ru.app.pharmacy.repositories

import androidx.lifecycle.LiveData
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.await
import ru.app.pharmacy.models.AddOrderResponse
import ru.app.pharmacy.models.Medicine
import ru.app.pharmacy.models.MedicineCart
import ru.app.pharmacy.models.MedicineDao
import ru.app.pharmacy.network.PharmacyAPI
import ru.app.pharmacy.repositories.manager.SharedPrefsManager

class MedicineRepository(
    private val pharmacyAPI: PharmacyAPI,
    private val medicineDao: MedicineDao,
    private val sharedPrefsManager: SharedPrefsManager
) {

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
        val res = pharmacyAPI.searchMedicine(
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

        return mergeCart(res.products)
    }

    suspend fun addOrder(products: List<MedicineCart>): AddOrderResponse {
        val jObject = JSONObject()
        val jArray = JSONArray()
        for (p in products) {
            val obj = JSONObject()
            obj.put("itemId", p.id)
            obj.put("count", p.count.value)
            jArray.put(obj)
        }
        jObject.put("order", jArray)
        jObject.put("pharmId", getProfile().pharmacyId)
        val json = RequestBody.create(MediaType.parse("application/json"), jObject.toString())
        return pharmacyAPI.addOrder(json).await()
    }

    private fun mergeCart(list: List<Medicine>): List<Medicine> {
        val cart = getCartItemsList()
        list.forEach { listItem ->
            cart.forEach { cartItem ->
                if (listItem.id == cartItem.id) listItem.count = cartItem.count
            }
        }
        return list
    }

    private fun getCartItemsList(): List<MedicineCart> = medicineDao.getAllList()

    fun getCartCount(): LiveData<Int> = medicineDao.getCount()

    fun getCartItems(): LiveData<List<MedicineCart>> = medicineDao.getAll()

    fun addCartItem(medicineCart: MedicineCart) = medicineDao.add(medicineCart)

    fun updateCartItem(medicineCart: MedicineCart) = medicineDao.update(medicineCart)

    fun deleteCartItem(medicineCart: MedicineCart) = medicineDao.delete(medicineCart)

    fun deleteAll() = medicineDao.deleteAll()

    fun getProfile() = sharedPrefsManager.getProfile()
}