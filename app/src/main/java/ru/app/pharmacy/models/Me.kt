package ru.app.pharmacy.models

data class MeResponse(val error: Int)

data class Me(
    val id: Int,
    val email: String,
    val fullName: String,
    val bdate: String,
    val level: Int
)