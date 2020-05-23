package ru.app.apteka.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.app.apteka.models.Order
import ru.app.apteka.network.NetworkState
import ru.app.apteka.repositories.OrderRepository
import ru.app.apteka.ui.base.BaseViewModel

class OrderModel(private val repository: OrderRepository) : BaseViewModel() {

    private var supervisorJob = SupervisorJob()
    private var _orders = MutableLiveData<List<Order>>()
    var orders: LiveData<List<Order>>
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>

    init {
        orders = _orders
        networkState = _networkState
        loadOrders()
    }

    fun loadOrders() {
        _networkState.postValue(NetworkState.RUNNING)
        ioScope.launch(getJobErrorHandler() + supervisorJob) {
            withContext(ioScope.coroutineContext){
                _orders.postValue(repository.getOrders())
                _networkState.postValue(NetworkState.SUCCESS)
            }
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.d("M__OrderModel", "Error: $e")
        _networkState.postValue(NetworkState.FAILED)
        supervisorJob.cancelChildren()
    }

    override fun onCleared() {
        super.onCleared()
        supervisorJob.cancelChildren()
    }
}