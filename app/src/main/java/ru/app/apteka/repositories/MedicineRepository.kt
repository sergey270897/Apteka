package ru.app.apteka.repositories

import android.util.Log
import ru.app.apteka.models.Medicine
import ru.app.apteka.utils.DataGenerator
import ru.app.apteka.utils.json2Medicine

class MedicineRepository {

    suspend fun getMedicine(
        q: String = "",
        categoryId: Int = 0,
        priceFrom: Int = 0,
        priceTo: Int = 20000,
        available: Boolean = false,
        count: Int = 20,
        offset: Int = 0,
        orderBy: String = "price",
        order: String = "asc"
    ): List<Medicine> {
        val response = DataGenerator.getMedicinesApi()
        var list = json2Medicine(response)
        if (categoryId != 0) list = list.filter { it.categoryId == categoryId }
        if (q.isNotEmpty()) list = list.filter { it.title.contains(q, true) }
        list = if(available) list.filter { it.available } else list
        list = list.filter { it.price >= priceFrom }
        list = list.filter { it.price <= priceTo }
        list = if(order == "asc") list.sortedBy { it.price } else list.sortedByDescending { it.price }
        //list = list.drop(offset).take(count)
        Thread.sleep(3000)
        return list
    }
}