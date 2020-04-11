package ru.app.apteka.utils

import org.json.JSONObject

val categories = """{
    "categories": [
    {
        "id": 1,
        "title": "Чаи и травы",
        "slug": "tea"
    },
    {
        "id": 2,
        "title": "Кардио устройства",
        "slug": "cardio"
    },
    {
        "id": 3,
        "title": "Массажеры",
        "slug": "massage"
    }
    ],
    "groups": [
    {
        "id": 1,
        "title": "БАДы",
        "categories": [1]
    },
    {
        "id": 2,
        "title": "Техника",
        "categories": [2, 3]
    }
    ]
}""".trimIndent()

val medicines = """
    {
      "count": 123,
      "products": [
        {
          "id": 1,
          "title": "Терафлю 1",
          "categoryId": 1,
          "categoryName": "Против гриппа",
          "image": "https://farmlend.ru/assets/thumbnails/d8/d881b2d1411c8e45a5271554f3b5cdcb.jpg",
          "price": 1234,
          "rating": 4,
          "available": true
        },
        {
          "id": 2,
          "title": "Терафлю 2",
          "categoryId": 1,
          "categoryName": "Против гриппа",
          "image": "https://farmlend.ru/assets/thumbnails/d8/d881b2d1411c8e45a5271554f3b5cdcb.jpg",
          "price": 1234,
          "rating": 4,
          "available": true
        },
        {
          "id": 3,
          "title": "Терафлю 3",
          "categoryId": 1,
          "categoryName": "Против гриппа",
          "image": "https://farmlend.ru/assets/thumbnails/d8/d881b2d1411c8e45a5271554f3b5cdcb.jpg",
          "price": 1234,
          "rating": 4,
          "available": true
        },
        {
          "id": 4,
          "title": "Терафлю 4",
          "categoryId": 2,
          "categoryName": "Против гриппа",
          "image": "https://farmlend.ru/assets/thumbnails/d8/d881b2d1411c8e45a5271554f3b5cdcb.jpg",
          "price": 100,
          "rating": 4,
          "available": true
        },
        {
          "id": 5,
          "title": "Терафлю 5",
          "categoryId": 2,
          "categoryName": "Против гриппа",
          "image": "https://farmlend.ru/assets/thumbnails/d8/d881b2d1411c8e45a5271554f3b5cdcb.jpg",
          "price": 200,
          "rating": 4,
          "available": true
        },
        {
          "id": 6,
          "title": "Терафлю 6",
          "categoryId": 2,
          "categoryName": "Против гриппа",
          "image": "https://farmlend.ru/assets/thumbnails/d8/d881b2d1411c8e45a5271554f3b5cdcb.jpg",
          "price": 300,
          "rating": 4,
          "available": true
        },
        {
          "id": 7,
          "title": "Терафлю 7",
          "categoryId": 3,
          "categoryName": "Против гриппа",
          "image": "https://farmlend.ru/assets/thumbnails/d8/d881b2d1411c8e45a5271554f3b5cdcb.jpg",
          "price": 1234,
          "rating": 4,
          "available": true
        },
        {
          "id": 8,
          "title": "Терафлю 8",
          "categoryId": 3,
          "categoryName": "Против гриппа",
          "image": "https://farmlend.ru/assets/thumbnails/d8/d881b2d1411c8e45a5271554f3b5cdcb.jpg",
          "price": 1234,
          "rating": 4,
          "available": true
        },
        {
          "id": 9,
          "title": "Терафлю 9",
          "categoryId": 3,
          "categoryName": "Против гриппа",
          "image": "https://farmlend.ru/assets/thumbnails/d8/d881b2d1411c8e45a5271554f3b5cdcb.jpg",
          "price": 1234,
          "rating": 4,
          "available": true
        },
        {
          "id": 10,
          "title": "Терафлю 10",
          "categoryId": 3,
          "categoryName": "Против гриппа",
          "image": "https://farmlend.ru/assets/thumbnails/d8/d881b2d1411c8e45a5271554f3b5cdcb.jpg",
          "price": 1234,
          "rating": 4,
          "available": true
        },
        {
          "id": 11,
          "title": "Терафлю 22.1г пор.д/р-ра д/пр.внутр. №10 пак. лимон",
          "categoryId": 2,
          "categoryName": "Против гриппа",
          "image": "https://farmlend.ru/assets/thumbnails/d8/d881b2d1411c8e45a5271554f3b5cdcb.jpg",
          "price": 1234,
          "rating": 4,
          "available": false
        }
      ]
    }

""".trimIndent()

object DataGenerator {

    fun getCategoriesApi() = JSONObject(categories)

    fun getMedicinesApi(
        q: String = "",
        categoryId: Int = 0,
        priceFrom: Float = 0F,                      // number >= 0
        priceTo: Float = Float.MAX_VALUE,
        available: Boolean = false,
        count: Int = 20,                            // 0 < number <= 100
        offset: Int = 0,                            // number >= 0
        orderBy: String = "rating",                 // "price" | "rating"
        order: String = "desc"                      // "asc" | "desc"
    ) = JSONObject(medicines)
}