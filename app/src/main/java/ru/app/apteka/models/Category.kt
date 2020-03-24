package ru.app.apteka.models

data class Category(
    val id: Int,
    val title: String,
    var parentId: Int? = null
)