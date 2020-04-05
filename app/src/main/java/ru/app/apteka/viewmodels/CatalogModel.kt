package ru.app.apteka.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.app.apteka.models.Category
import ru.app.apteka.network.NetworkState
import ru.app.apteka.repositories.CatalogRepository
import ru.app.apteka.ui.base.BaseViewModel

class CatalogModel() : BaseViewModel() {

    private val repository = CatalogRepository()

    lateinit var allCategories: LiveData<List<Category>>

    private var _categories = MutableLiveData<List<Category>>()
    var categories: LiveData<List<Category>>

    // private val _isLoading = MutableLiveData(false)
    // val isLoading: LiveData<Boolean>

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>

    init {
        categories = _categories
        // isLoading = _isLoading
        networkState = _networkState
        loadCategories()
    }

    fun loadCategories() {
        // _isLoading.value = true
        _networkState.postValue(NetworkState.RUNNING)
        uiScope.launch(getJobErrorHandler()) {
            withContext(ioScope.coroutineContext) {
                allCategories = repository.getCategories()
                _networkState.postValue(NetworkState.SUCCESS)
            }
            //_isLoading.value = false
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.d("M__CatalogModel", "Error: $e")
        _networkState.postValue(NetworkState.FAILED)
    }

    fun getGroups() {
        _categories.value = allCategories.value?.filter { it.parentId == null }
    }

    fun getCategories(id: Int) {
        _categories.value = allCategories.value?.filter { it.parentId == id }
    }
}