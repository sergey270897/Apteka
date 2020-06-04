package ru.app.pharmacy.models

import java.text.SimpleDateFormat
import java.util.*

data class Profile(
    var token: String?,
    var refresh: String?,
    var name: String?,
    var email: String?,
    var bd: String?
) {
    var pharmacyId: Int = 0

    fun setDate(date: Date) {
        val newFormat = SimpleDateFormat("dd-MM-yyyy", Locale("ru"))
        bd = newFormat.format(date)
    }

    fun getDate(): Date {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale("ru"))
        return dateFormat.parse(bd)
    }
}

data class EmailResponse(val email: String, val error: Int)

data class AuthResponse(val accessToken: String, val refreshToken: String)