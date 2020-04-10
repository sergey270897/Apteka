package ru.app.apteka.repositories

import ru.app.apteka.models.Medicine
import ru.app.apteka.utils.DataGenerator
import ru.app.apteka.utils.json2Medicine

class MedicineRepository {

    suspend fun getMedicine(
        q: String = "",
        categoryId: Int = 0,
        priceFrom: Float = 0F,
        priceTo: Float = Float.MAX_VALUE,
        available: Boolean = false,
        count: Int = 20,
        offset: Int = 0,
        orderBy: String = "rating",
        order: String = "desc"
    ): List<Medicine> {
        val response = DataGenerator.getMedicinesApi()
        var list = json2Medicine(response)
        if (categoryId != 0) list = list.filter { it.categoryId == categoryId }
        if (q.isNotEmpty()) list = list.filter { it.title.contains(q, true) }
        list = list.drop(offset).take(count)
        Thread.sleep(3000)
        return list
    }
}