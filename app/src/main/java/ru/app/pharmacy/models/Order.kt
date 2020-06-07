package ru.app.pharmacy.models

import java.text.SimpleDateFormat
import java.util.*

data class AddOrderResponse(val error: Int, val msg: String, val order: List<OrderItem>)

data class OrderItem(val itemId: Long, val count: Int)

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
        val time = dateFormat.parse(date).time + TimeZone.getDefault().rawOffset
        val newFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("ru"))
        return newFormat.format(time)
    }

    fun getStatusString(): String = when (status) {
        10 -> "Оформлен"
        20 -> "Выдан"
        30 -> "Отменен"
        else -> "Ожидает обработки"
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