package ru.app.apteka.models

import androidx.lifecycle.MutableLiveData

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
    var count = MutableLiveData(0)
    fun priceString():String = String.format("%.2f ₽", price)
}