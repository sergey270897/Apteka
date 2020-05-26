package ru.app.pharmacy.models

data class Profile(
    var token: String?,
    var refresh: String?,
    var name: String?,
    var email: String?
) {
    var pharmacyId: Int = 0
}

data class EmailResponse(val email: String, val error: Int)

data class AuthResponse(val accessToken: String, val refreshToken: String)