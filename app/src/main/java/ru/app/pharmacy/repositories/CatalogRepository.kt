package ru.app.pharmacy.repositories

import retrofit2.await
import ru.app.pharmacy.models.Category
import ru.app.pharmacy.models.toCategory
import ru.app.pharmacy.network.PharmacyAPI

class CatalogRepository(private val pharmacyAPI: PharmacyAPI) {
    suspend fun getCategories(): List<Category> {
        val res = pharmacyAPI.getCategories().await()
        return res.toCategory()
    }
}