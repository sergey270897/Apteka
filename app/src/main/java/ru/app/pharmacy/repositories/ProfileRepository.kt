package ru.app.pharmacy.repositories

import retrofit2.await
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
}