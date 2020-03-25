package ru.app.apteka.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.app.apteka.models.Category
import ru.app.apteka.repositories.CatalogRepository

class CatalogModel(application: Application) : AndroidViewModel(application) {

    private val repository = CatalogRepository()
    private lateinit var allCategories: LiveData<List<Category>>
    private var _categories = MutableLiveData<List<Category>>()
    var categories: LiveData<List<Category>>
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>

    init {
        categories = _categories
        isLoading = _isLoading
        loadCategories()
    }

    private fun loadCategories() {
        _isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                allCategories = repository.getCategories()
            }
            _isLoading.value = false
        }
    }

    fun getGroups() {
        _categories.value = allCategories.value?.filter { it.parentId == null }
    }

    fun getCategories(id: Int) {
        _categories.value = allCategories.value?.filter { it.parentId == id }
    }
}