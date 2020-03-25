package ru.app.apteka.models

data class Medicine(
    val id:Int,
    val title:String,
    val categoryId:Int,
    val categoryName:String,
    val image:String,
    val price:Int,
    val rating:Int,
    val available: Boolean
)