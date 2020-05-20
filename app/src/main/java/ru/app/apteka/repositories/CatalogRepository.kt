package ru.app.apteka.repositories

import android.util.Log
import retrofit2.await
import ru.app.apteka.models.Category
import ru.app.apteka.models.toCategory
import ru.app.apteka.network.AptekaAPI

class CatalogRepository(private val aptekaAPI: AptekaAPI) {

    suspend fun getCategories(): List<Category> {
        val res = aptekaAPI.getCategories().await()
        return res.toCategory()
    }
}