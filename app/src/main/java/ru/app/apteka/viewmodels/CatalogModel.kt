package ru.app.apteka.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.app.apteka.models.Category
import ru.app.apteka.network.NetworkState
import ru.app.apteka.repositories.CatalogRepository
import ru.app.apteka.ui.base.BaseViewModel

class CatalogModel(private val repository: CatalogRepository) : BaseViewModel() {

    private var supervisorJob = SupervisorJob()

    private lateinit var allCategories: List<Category>

    private var _categories = MutableLiveData<List<Category>>()
    var categories: LiveData<List<Category>>

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>

    init {
        categories = _categories
        networkState = _networkState
        loadCategories()
    }

    fun loadCategories() {
        _networkState.postValue(NetworkState.RUNNING)
        uiScope.launch(getJobErrorHandler() + supervisorJob) {
            withContext(ioScope.coroutineContext) {
                allCategories = repository.getCategories()
                _networkState.postValue(NetworkState.SUCCESS)
            }
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.d("M__CatalogModel", "Error: $e")
        _networkState.postValue(NetworkState.FAILED)
        supervisorJob.cancelChildren()
    }

    fun getGroups() {
        _categories.value = allCategories.filter { it.parentId == null }
    }

    fun getCategories(id: Int) {
        _categories.value = allCategories.filter { it.parentId == id }
    }

    override fun onCleared() {
        super.onCleared()
        supervisorJob.cancelChildren()
    }
}