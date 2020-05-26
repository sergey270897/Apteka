package ru.app.pharmacy.repositories

import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.app.pharmacy.BuildConfig
import ru.app.pharmacy.models.AuthResponse
import ru.app.pharmacy.models.Profile
import ru.app.pharmacy.network.PharmacyAPI
import ru.app.pharmacy.repositories.manager.SharedPrefsManager

class TokenRepository(private val sharedPrefsManager: SharedPrefsManager) {
    fun getProfile() = sharedPrefsManager.getProfile()

    fun refresh(refreshToken: String): Call<AuthResponse> {
        val rt = RequestBody.create(MediaType.parse("text/plain"), refreshToken)
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(PharmacyAPI::class.java).refresh(rt)
    }

    fun saveProfile(profile: Profile) {
        sharedPrefsManager.saveProfile(profile)
    }
}