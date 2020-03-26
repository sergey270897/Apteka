package ru.app.apteka.models

data class Medicine(
    val id:Int,
    val title:String,
    val categoryId:Int,
    val categoryName:String,
    val image:String,
    val price:Float,
    val rating:Float,
    val available: Boolean
){
    fun priceString():String = String.format("%.2f ₽", price)
}