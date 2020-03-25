package ru.app.apteka.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.app.apteka.R
import ru.app.apteka.models.Category
import ru.app.apteka.repositories.CatalogRepository

class CatalogModel(application: Application) : AndroidViewModel(application) {

    private lateinit var allCategories: LiveData<List<Category>>
    private val repository = CatalogRepository()
    private var _categories = MutableLiveData<List<Category>>()
    var categories: LiveData<List<Category>>

    val isLoading = MutableLiveData(false)
    val showBack = MutableLiveData(false)
    val showSearch = MutableLiveData(true)
    val title = MutableLiveData("")
    private var currentCategory: Category? = null
    private val defaultTitle = application.getString(R.string.app_name)

    init {
        categories = _categories
        loadCategories()
        title.value = defaultTitle
    }

    private fun loadCategories() {
        isLoading.value = true
        showSearch.value = false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                allCategories = repository.getCategories()
            }
            getGroups()
            isLoading.value = false
            showSearch.value = true
        }
    }

    private fun getGroups() {
        _categories.value = allCategories.value?.filter { it.parentId == null }
    }

    private fun getCategories(id: Int) {
        _categories.value = allCategories.value?.filter { it.parentId == id }
    }

    fun onClick(category: Category) {
        if (category.parentId == null) {
            showBack.value = true
            getCategories(category.id)
            currentCategory = category.copy(parentId = -1)
        } else {
            showSearch.value = false
            currentCategory?.parentId = -2
            _categories.value = listOf()
        }
        title.value = category.title
    }

    fun onClickBack(): Boolean {
        showSearch.value = true
        when {
            currentCategory == null -> { return true }
            currentCategory?.parentId == -1 -> {
                currentCategory = null
                getGroups()
                showBack.value = false
                title.value = defaultTitle
            }
            currentCategory?.parentId == -2 -> {
                getCategories(currentCategory?.id!!)
                title.value = currentCategory?.title
                currentCategory?.parentId = -1
            }
        }
        return false
    }
}