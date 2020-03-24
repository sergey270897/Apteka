package ru.app.apteka.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.app.apteka.models.Category
import ru.app.apteka.repositories.CatalogRepository

class CatalogModel : ViewModel() {

    private lateinit var allCategories: LiveData<List<Category>>
    private val repository = CatalogRepository()
    private var _categories = MutableLiveData<List<Category>>()
    var categories: LiveData<List<Category>>
    val isLoading = MutableLiveData(false)
    val showBack = MutableLiveData(false)

    init{
        categories = _categories
        loadCategories()
    }

    private fun loadCategories() {
        isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                allCategories = repository.getCategories()
            }
            getGroups()
            isLoading.value = false
        }
    }

    private fun getGroups() {
        _categories.value = allCategories.value?.filter { it.parentId == null }
    }

    private fun getCategories(id:Int) {
        _categories.value = allCategories.value?.filter { it.parentId == id }
    }

    fun onClick(category:Category){
        if(category.parentId== null){
            showBack.value = true
            getCategories(category.id)
        }else{

        }
    }

    fun onClickBack(){
        showBack.value = false
        getGroups()
    }
}