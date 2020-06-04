package ru.app.pharmacy.repositories

import android.util.Log
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.await
import ru.app.pharmacy.models.Me
import ru.app.pharmacy.models.MeResponse
import ru.app.pharmacy.models.Pharmacy
import ru.app.pharmacy.models.Profile
import ru.app.pharmacy.network.PharmacyAPI
import ru.app.pharmacy.repositories.manager.SharedPrefsManager

class ProfileRepository(
    private val pharmacyAPI: PharmacyAPI,
    private val sharedPrefsManager: SharedPrefsManager
) {
    suspend fun getPharmacy(): List<Pharmacy> {
        return pharmacyAPI.getPharmacy().await().pharmacy
    }

    fun getProfile(): Profile = sharedPrefsManager.getProfile()

    fun saveProfile(profile: Profile) = sharedPrefsManager.saveProfile(profile)


    suspend fun changeMe(name:String, bd:String): MeResponse {
        val nameRequest = RequestBody.create(MediaType.parse("text/plain"), name)
        val bdRequest = RequestBody.create(MediaType.parse("text/plain"), bd)
        return pharmacyAPI.changeMe(nameRequest, bdRequest).await()
    }

    suspend fun getMe(): Me = pharmacyAPI.getMe().await()
}