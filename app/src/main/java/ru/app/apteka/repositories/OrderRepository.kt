package ru.app.apteka.repositories

import retrofit2.await
import ru.app.apteka.models.Order
import ru.app.apteka.network.AptekaAPI

class OrderRepository(private val api: AptekaAPI) {
    suspend fun getOrders(): List<Order> = api.getOrders().await().orders
}