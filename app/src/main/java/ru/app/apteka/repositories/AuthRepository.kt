package ru.app.apteka.repositories

import android.util.Log
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.await
import ru.app.apteka.models.AuthResponse
import ru.app.apteka.models.EmailResponse
import ru.app.apteka.network.AptekaAPI
import java.lang.Exception

class AuthRepository(private val aptekaAPI: AptekaAPI) {
    suspend fun sendEmail(email:String): EmailResponse{
        val emailRequest = RequestBody.create(MediaType.parse("text/plain"), email)
        return aptekaAPI.sendEmail(emailRequest).await()
    }

    suspend fun auth(email: String, secretNum:String): AuthResponse{
        val emailRequest = RequestBody.create(MediaType.parse("text/plain"), email)
        val secretNumRequest = RequestBody.create(MediaType.parse("text/plain"), secretNum)
        return aptekaAPI.auth(emailRequest, secretNumRequest).await()
    }
}

