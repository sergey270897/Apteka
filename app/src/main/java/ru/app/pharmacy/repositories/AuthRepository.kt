package ru.app.pharmacy.repositories

import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.await
import ru.app.pharmacy.models.AuthResponse
import ru.app.pharmacy.models.EmailResponse
import ru.app.pharmacy.network.PharmacyAPI

class AuthRepository(private val pharmacyAPI: PharmacyAPI) {
    suspend fun sendEmail(email: String): EmailResponse {
        val emailRequest = RequestBody.create(MediaType.parse("text/plain"), email)
        return pharmacyAPI.sendEmail(emailRequest).await()
    }

    suspend fun auth(email: String, secretNum: String): AuthResponse {
        val emailRequest = RequestBody.create(MediaType.parse("text/plain"), email)
        val secretNumRequest = RequestBody.create(MediaType.parse("text/plain"), secretNum)
        return pharmacyAPI.auth(emailRequest, secretNumRequest).await()
    }

}