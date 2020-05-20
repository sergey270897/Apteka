package ru.app.apteka.viewmodels

import androidx.lifecycle.MutableLiveData
import ru.app.apteka.models.Profile
import ru.app.apteka.repositories.ProfileRepository
import ru.app.apteka.ui.base.BaseViewModel

class ProfileModel(private val repository:ProfileRepository):BaseViewModel(){

    val profile = repository.getProfile()
    val isEdit = MutableLiveData(false)

    fun saveProfile(){
        repository.saveProfile(profile)
    }

    fun exitProfile(){
        repository.saveProfile(Profile(null, null, null, null))
    }
}