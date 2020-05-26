package ru.app.pharmacy.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.app.pharmacy.models.Pharmacy
import ru.app.pharmacy.models.Profile
import ru.app.pharmacy.network.NetworkState
import ru.app.pharmacy.repositories.ProfileRepository
import ru.app.pharmacy.ui.base.BaseViewModel

class ProfileModel(private val repository: ProfileRepository) : BaseViewModel() {

    private val _networkState = MutableLiveData<NetworkState>()
    private var supervisorJob = SupervisorJob()
    val networkState: LiveData<NetworkState>
    var listPharmacy = MutableLiveData(listOf<Pharmacy>())
    val profile = repository.getProfile()
    val isEdit = MutableLiveData(false)

    init {
        getPharmacy()
        networkState = _networkState
    }

    override fun onCleared() {
        super.onCleared()
        supervisorJob.cancelChildren()
    }

    private fun getPharmacy() {
        _networkState.postValue(NetworkState.RUNNING)
        ioScope.launch(getJobErrorHandler() + supervisorJob) {
            withContext(ioScope.coroutineContext) {
                listPharmacy.postValue(repository.getPharmacy())
                _networkState.postValue(NetworkState.SUCCESS)
            }
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        _networkState.postValue(NetworkState.FAILED)
        supervisorJob.cancelChildren()
    }

    fun saveProfile() {
        repository.saveProfile(profile)
    }

    fun exitProfile() {
        repository.saveProfile(Profile(null, null, null, null))
    }
}