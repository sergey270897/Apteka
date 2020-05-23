package ru.app.apteka.repositories

import retrofit2.await
import ru.app.apteka.models.Pharmacy
import ru.app.apteka.models.Profile
import ru.app.apteka.network.AptekaAPI
import ru.app.apteka.repositories.manager.SharedPrefsManager

class ProfileRepository(
    private val aptekaAPI: AptekaAPI,
    private val sharedPrefsManager: SharedPrefsManager
) {
    fun getProfile(): Profile = sharedPrefsManager.getProfile()
    fun saveProfile(profile: Profile) = sharedPrefsManager.saveProfile(profile)

    suspend fun getPharmacy(): List<Pharmacy> {
        return aptekaAPI.getPharmacy().await().pharmacy
    }
}