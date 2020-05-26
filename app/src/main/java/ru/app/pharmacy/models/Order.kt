package ru.app.pharmacy.models

import java.text.SimpleDateFormat
import java.util.Locale

data class AddOrderResponse(val error: Int, val msg: String)

data class OrderResponse(val orders: List<Order>)

data class Order(
    val id: Long,
    val pharmId: Long,
    val date: String,
    val status: Int,
    val products: List<Product>
) {
    fun getDateString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale("ru"))
        val d = dateFormat.parse(date)
        val newFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("ru"))
        return newFormat.format(d)
    }

    fun getStatusString(): String = when (status) {
        10 -> "Оформлен"
        20 -> "Выдан"
        30 -> "Отменен"
        else -> "Статус не указан"
    }
}

data class Product(
    val id: Long,
    val count: Int,
    val title: String,
    val image: String,
    val price: Float
) {
    fun priceString(): String = String.format("%.2f ₽", price)
    fun countString(): String = "$count шт."
}