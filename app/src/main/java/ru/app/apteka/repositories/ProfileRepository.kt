package ru.app.apteka.repositories

import ru.app.apteka.models.Profile
import ru.app.apteka.repositories.manager.SharedPrefsManager

class ProfileRepository(private val sharedPrefsManager: SharedPrefsManager){
    fun getProfile():Profile = sharedPrefsManager.getProfile()
    fun saveProfile(profile: Profile) = sharedPrefsManager.saveProfile(profile)
}