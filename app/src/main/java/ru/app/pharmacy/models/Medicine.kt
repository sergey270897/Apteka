package ru.app.pharmacy.models

import androidx.lifecycle.MutableLiveData

data class Medicine(
    val id: Long,
    val title: String,
    val categoryId: Long,
    val categoryName: String,
    val image: String = "",
    val price: Float,
    val rating: Float,
    val available: Boolean,
    val description: String,
    var balance:Int
) {
    var count: MutableLiveData<Int> = MutableLiveData(0)
        get() {
            if (field == null) field = MutableLiveData(0)
            return field
        }

    fun priceString(): String = String.format("%.2f ₽", price)
    fun toMedicineCart(): MedicineCart = MedicineCart(
        id,
        title,
        image,
        price,
        count,
        categoryId,
        categoryName,
        rating,
        available,
        description,
        balance
    )
}

data class MedicineResponse(val count: Int, val products: List<Medicine>)