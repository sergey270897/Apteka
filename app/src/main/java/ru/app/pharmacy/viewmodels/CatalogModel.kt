package ru.app.pharmacy.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.app.pharmacy.models.Category
import ru.app.pharmacy.network.NetworkState
import ru.app.pharmacy.repositories.CatalogRepository
import ru.app.pharmacy.ui.base.BaseViewModel

class CatalogModel(private val repository: CatalogRepository) : BaseViewModel() {

    private var supervisorJob = SupervisorJob()
    private lateinit var allCategories: List<Category>
    private val _networkState = MutableLiveData<NetworkState>()
    private var _categories = MutableLiveData<List<Category>>()
    var categories: LiveData<List<Category>>
    val networkState: LiveData<NetworkState>

    init {
        categories = _categories
        networkState = _networkState
        loadCategories()
    }

    override fun onCleared() {
        super.onCleared()
        supervisorJob.cancelChildren()
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        _networkState.postValue(NetworkState.FAILED)
        supervisorJob.cancelChildren()
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

    fun getGroups() {
        _categories.value = allCategories.filter { it.parentId == null }
    }

    fun getCategories(id: Int) {
        _categories.value = allCategories.filter { it.parentId == id }
    }
}