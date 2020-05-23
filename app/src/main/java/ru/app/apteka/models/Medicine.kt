package ru.app.apteka.models

import androidx.lifecycle.MutableLiveData

data class Medicine(
    val id: Long,
    val name: String,
    val categoryId: Int,
    val categoryName: String,
    val image: String = "",
    val price: Float,
    val rating: Float,
    val available: Boolean
) {
    var count: MutableLiveData<Int> = MutableLiveData(0)
        get() {
            if (field == null) field = MutableLiveData(0)
            return field
        }

    fun priceString(): String = String.format("%.2f ₽", price)
    fun toMedicineCart(): MedicineCart = MedicineCart(id, name, image, price, count)
}

data class MedicineResponse(val count: Int, val products: List<Medicine>)