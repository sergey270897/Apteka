package ru.app.apteka.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.app.apteka.models.Category
import ru.app.apteka.utils.DataGenerator
import ru.app.apteka.utils.Utils

class CatalogRepository {

    suspend fun getCategories(): LiveData<List<Category>> {
        val response = DataGenerator.getCategoriesApi()
        Thread.sleep(2000)
        return MutableLiveData(Utils.json2Category(response))
    }
}