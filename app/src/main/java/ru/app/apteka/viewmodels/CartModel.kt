package ru.app.apteka.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.app.apteka.models.MedicineCart
import ru.app.apteka.repositories.MedicineRepository
import ru.app.apteka.repositories.manager.SharedPrefsManager
import ru.app.apteka.ui.base.BaseViewModel

class CartModel(
    private val repository: MedicineRepository,
    private val sharedPrefsManager: SharedPrefsManager
) : BaseViewModel() {

    val total = MutableLiveData(0F)
    val startActivityAuth = MutableLiveData(false)

    val count = repository.getCartCount()

    fun setTotal(list: List<MedicineCart>) {
        total.value = 0f
        for (a in list) {
            total.value = total.value?.plus(a.price * a.count.value!!)
        }
    }

    fun getCartItems(): LiveData<List<MedicineCart>> = repository.getCartItems()

    fun deleteCartItem(item: MedicineCart) {
        ioScope.launch {
            repository.deleteCartItem(item)
        }
    }

    fun updateCartItem(item: MedicineCart) {
        ioScope.launch {
            repository.updateCartItem(item)
        }
    }

    fun onClickOrder() {
        if(checkLogin()){

        }
    }

    fun checkLogin(): Boolean {
        val profile = sharedPrefsManager.getProfile()
        return if (profile.token.isNullOrEmpty()) {
            startActivityAuth.value = true
            false
        } else
            true
    }
}