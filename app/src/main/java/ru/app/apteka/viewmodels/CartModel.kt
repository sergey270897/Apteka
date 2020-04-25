package ru.app.apteka.viewmodels

import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import ru.app.apteka.models.MedicineCart
import ru.app.apteka.repositories.MedicineRepository
import ru.app.apteka.ui.base.BaseViewModel

class CartModel(private val repository: MedicineRepository) : BaseViewModel() {
    fun getCartItems(): LiveData<List<MedicineCart>> = repository.getCartItems()

    fun deleteCartItem(item: MedicineCart) {
        ioScope.launch {
            repository.deleteCartItem(item)
        }
    }

    fun addCartItem(item: MedicineCart) {
        ioScope.launch {
            repository.addCartItem(item)
        }
    }

    fun updateCartItem(item: MedicineCart) {
        ioScope.launch {
            repository.updateCartItem(item)
        }
    }
}