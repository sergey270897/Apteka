package ru.app.pharmacy.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.app.pharmacy.models.Order
import ru.app.pharmacy.network.NetworkState
import ru.app.pharmacy.repositories.OrderRepository
import ru.app.pharmacy.ui.base.BaseViewModel

class OrderModel(private val repository: OrderRepository) : BaseViewModel() {

    private var supervisorJob = SupervisorJob()
    private var _orders = MutableLiveData<List<Order>>()
    private val _networkState = MutableLiveData<NetworkState>()
    var orders: LiveData<List<Order>>
    val networkState: LiveData<NetworkState>

    init {
        orders = _orders
        networkState = _networkState
        loadOrders()
    }

    override fun onCleared() {
        super.onCleared()
        supervisorJob.cancelChildren()
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        _networkState.postValue(NetworkState.FAILED)
        supervisorJob.cancelChildren()
    }

    fun loadOrders() {
        _networkState.postValue(NetworkState.RUNNING)
        ioScope.launch(getJobErrorHandler() + supervisorJob) {
            withContext(ioScope.coroutineContext) {
                _orders.postValue(repository.getOrders())
                _networkState.postValue(NetworkState.SUCCESS)
            }
        }
    }
}