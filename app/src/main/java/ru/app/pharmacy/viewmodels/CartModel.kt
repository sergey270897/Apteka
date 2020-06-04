package ru.app.pharmacy.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import ru.app.pharmacy.models.MedicineCart
import ru.app.pharmacy.network.NetworkState
import ru.app.pharmacy.repositories.MedicineRepository
import ru.app.pharmacy.ui.base.BaseViewModel

class CartModel(
    private val repository: MedicineRepository
) : BaseViewModel() {

    private val _msg = MutableLiveData(0 to "")
    private val products: LiveData<List<MedicineCart>> = repository.getCartItems()
    private val _networkState = MutableLiveData<NetworkState>()
    private var supervisorJob = SupervisorJob()
    val networkState: LiveData<NetworkState>
    val msg: LiveData<Pair<Int, String>>
    val total = MutableLiveData(0F)
    val startActivityAuth = MutableLiveData(false)
    val count = repository.getCartCount()

    init {
        networkState = _networkState
        msg = _msg
    }

    override fun onCleared() {
        super.onCleared()
        supervisorJob.cancelChildren()
    }

    private fun addOrder() {
        val profile = repository.getProfile()
        if (profile.pharmacyId < 1) {
            _msg.postValue(2 to "Warning")
            return
        }
        _networkState.postValue(NetworkState.RUNNING)
        ioScope.launch(getErrorHandler() + supervisorJob) {
            val res = repository.addOrder(products.value!!)
            _msg.postValue(1 to res.msg)
            _networkState.postValue(NetworkState.SUCCESS)
            deleteAll()
        }
    }

    private fun getErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.d("M__CartModel", "An error happened: $e")
        _networkState.postValue(NetworkState.FAILED)
        _msg.postValue(3 to "Error")
        supervisorJob.cancelChildren()
    }

    fun setTotal(list: List<MedicineCart>) {
        total.value = 0f
        for (a in list) {
            total.value = total.value?.plus(a.price * a.count.value!!)
        }
    }

    fun getCartItems(): LiveData<List<MedicineCart>> = products

    fun deleteCartItem(item: MedicineCart) {
        ioScope.launch {
            repository.deleteCartItem(item)
        }
    }

    private fun deleteAll() {
        ioScope.launch {
            repository.deleteAll()
        }
    }

    fun updateCartItem(item: MedicineCart) {
        ioScope.launch {
            repository.updateCartItem(item)
        }
    }

    fun onClickOrder() {
        if (checkLogin()) {
            addOrder()
        }
    }

    fun clearMsgSnackBar() {
        _msg.postValue(0 to "")
    }

    fun checkLogin(): Boolean {
        val profile = repository.getProfile()
        return if (profile.token.isNullOrEmpty()) {
            startActivityAuth.value = true
            false
        } else {
            true
        }
    }
}