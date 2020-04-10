package ru.app.apteka.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.app.apteka.models.Category
import ru.app.apteka.utils.DataGenerator
import ru.app.apteka.utils.json2Category

class CatalogRepository {

    suspend fun getCategories(): LiveData<List<Category>> {
        val response = DataGenerator.getCategoriesApi()
        Thread.sleep(2000)
        return MutableLiveData(json2Category(response))
    }
}