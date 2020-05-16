package ru.app.apteka.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.app.apteka.models.MedicineCart
import ru.app.apteka.repositories.MedicineRepository
import ru.app.apteka.ui.base.BaseViewModel

class CartModel(private val repository: MedicineRepository) : BaseViewModel() {

    val total = MutableLiveData(0F)

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
}