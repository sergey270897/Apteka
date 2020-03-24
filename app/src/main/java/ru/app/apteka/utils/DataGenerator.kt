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
    }
    ]
}"""

object DataGenerator {
    fun getCategoriesApi() = JSONObject(categories)
}