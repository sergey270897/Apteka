package ru.app.pharmacy.repositories

import retrofit2.await
import ru.app.pharmacy.models.Order
import ru.app.pharmacy.network.PharmacyAPI

class OrderRepository(private val api: PharmacyAPI) {
    suspend fun getOrders(): List<Order> = api.getOrders().await().orders
}