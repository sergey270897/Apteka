package ru.app.pharmacy.models

data class Filter(
    var sortIndex: Int,
    var priceFrom: Int,
    var priceTo: Int,
    var available: Boolean
) {
    fun priceFromString(): String = "$priceFrom ₽"
    fun priceToString(): String = "$priceTo ₽"
}