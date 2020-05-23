package ru.app.apteka.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.app.apteka.models.Pharmacy
import ru.app.apteka.models.Profile
import ru.app.apteka.network.NetworkState
import ru.app.apteka.repositories.ProfileRepository
import ru.app.apteka.ui.base.BaseViewModel

class ProfileModel(private val repository:ProfileRepository):BaseViewModel(){

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
    private var supervisorJob = SupervisorJob()
    var listPharmacy = MutableLiveData(listOf<Pharmacy>())
    val profile = repository.getProfile()
    val isEdit = MutableLiveData(false)

    init {
        getPharmacy()
        networkState = _networkState
    }

    fun saveProfile(){
        repository.saveProfile(profile)
    }

    fun exitProfile(){
        repository.saveProfile(Profile(null, null, null, null))
    }

    private fun getPharmacy(){
        _networkState.postValue(NetworkState.RUNNING)
        ioScope.launch(getJobErrorHandler() + supervisorJob) {
            withContext(ioScope.coroutineContext){
                listPharmacy.postValue(repository.getPharmacy())
                _networkState.postValue(NetworkState.SUCCESS)
            }
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.d("M__ProfileModel", "Error: $e")
        _networkState.postValue(NetworkState.FAILED)
        supervisorJob.cancelChildren()
    }

    override fun onCleared() {
        super.onCleared()
        supervisorJob.cancelChildren()
    }

}