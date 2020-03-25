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
    },
    {
        "id": 4,
        "title": "Массажеры asdsad",
        "slug": "massage"
    },
    {
        "id": 5,
        "title": "Массажеры sdsdsd999",
        "slug": "massage"
    },
    {
        "id": 6,
        "title": "Массажеры sdsdsd999",
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
    },
    {
        "id": 3,
        "title": "Техника asas",
        "categories": [4, 5]
    },
    {
        "id": 4,
        "title": "Техника asas",
        "categories": [6]
    },
    {
        "id": 4,
        "title": "Техника asas",
        "categories": [6]
    },
    {
        "id": 4,
        "title": "Техника asas",
        "categories": [6]
    },
    {
        "id": 4,
        "title": "Техника asas",
        "categories": [6]
    },
    {
        "id": 4,
        "title": "Техника asas",
        "categories": [6]
    },
    {
        "id": 4,
        "title": "Техника asas",
        "categories": [6]
    },
    {
        "id": 4,
        "title": "Техника asas",
        "categories": [6]
    },
    {
        "id": 4,
        "title": "Техника asas",
        "categories": [6]
    },
    {
        "id": 4,
        "title": "Техника asas",
        "categories": [6]
    }
    ]
}""".trimIndent()

val medicines = """
    {
      "count": 123,
      "products": [
        {
          "id": 1,
          "title": "Терафлю 22.1г пор.д/р-ра д/пр.внутр. №10 пак. лимон",
          "categoryId": 1,
          "categoryName": "Против гриппа",
          "image": "https://farmlend.ru/assets/thumbnails/d8/d881b2d1411c8e45a5271554f3b5cdcb.jpg",
          "price": 1234,
          "rating": 4,
          "available": true
        },
        {
          "id": 2,
          "title": "Терафлю 22.1г пор.д/р-ра д/пр.внутр. №10 пак. лимон",
          "categoryId": 1,
          "categoryName": "Против гриппа",
          "image": "https://farmlend.ru/assets/thumbnails/d8/d881b2d1411c8e45a5271554f3b5cdcb.jpg",
          "price": 1234,
          "rating": 4,
          "available": true
        },
        {
          "id": 3,
          "title": "Терафлю 22.1г пор.д/р-ра д/пр.внутр. №10 пак. лимон",
          "categoryId": 1,
          "categoryName": "Против гриппа",
          "image": "https://farmlend.ru/assets/thumbnails/d8/d881b2d1411c8e45a5271554f3b5cdcb.jpg",
          "price": 1234,
          "rating": 4,
          "available": true
        },
        {
          "id": 4,
          "title": "Терафлю 22.1г пор.д/р-ра д/пр.внутр. №10 пак. лимон",
          "categoryId": 1,
          "categoryName": "Против гриппа",
          "image": "https://farmlend.ru/assets/thumbnails/d8/d881b2d1411c8e45a5271554f3b5cdcb.jpg",
          "price": 1234,
          "rating": 4,
          "available": true
        },
        {
          "id": 5,
          "title": "Терафлю 22.1г пор.д/р-ра д/пр.внутр. №10 пак. лимон",
          "categoryId": 1,
          "categoryName": "Против гриппа",
          "image": "https://farmlend.ru/assets/thumbnails/d8/d881b2d1411c8e45a5271554f3b5cdcb.jpg",
          "price": 1234,
          "rating": 4,
          "available": true
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