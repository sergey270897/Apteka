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
        return getPharmacyData()
        //return aptekaAPI.getPharmacy().await().pharmacy
    }

    fun getPharmacyData():List<Pharmacy>{
        Thread.sleep(3000)
        val list = listOf<Pharmacy>(
            Pharmacy(1, "First", "Tyumen"),
            Pharmacy(2, "Second", "Perekopska 15a"),
            Pharmacy(3, "Third apteka name long long very long hohohoh", "Nema City Tyumen, Perekopska 15a street")
        )
        return list
    }
}