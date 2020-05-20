package ru.app.apteka.viewmodels

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import ru.app.apteka.models.Profile
import ru.app.apteka.network.NetworkState
import ru.app.apteka.repositories.AuthRepository
import ru.app.apteka.repositories.manager.SharedPrefsManager
import ru.app.apteka.ui.base.BaseViewModel

class AuthModel(
    private val repository: AuthRepository,
    private val sharedPrefsManager: SharedPrefsManager
) :
    BaseViewModel() {

    private var supervisorJob = SupervisorJob()

    private val MINUTE = 60000L
    private val SECOND = 1000L
    val time = MutableLiveData(MINUTE / SECOND)
    private lateinit var timer: CountDownTimer

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>

    val profile = Profile(null, null, null, null)
    val finishAuth = MutableLiveData(false)

    init {
        initTimer()
        networkState = _networkState
    }

    private fun initTimer() {
        timer = object : CountDownTimer(MINUTE, SECOND) {
            override fun onFinish() {
                time.value = 0
            }

            override fun onTick(millisUntilFinished: Long) {
                time.value = millisUntilFinished / SECOND
            }
        }
    }

    fun startTimer() {
        time.value = MINUTE / SECOND
        timer.start()
    }

    fun stopTimer() {
        timer.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        supervisorJob.cancelChildren()
    }

    fun sendEmail(email: String) {
        _networkState.postValue(NetworkState.RUNNING)
        ioScope.launch(getErrorHandler() + supervisorJob) {
            repository.sendEmail(email)
            _networkState.postValue(NetworkState.SUCCESS)
        }
    }

    fun auth(email: String, secretNum: String) {
        _networkState.postValue(NetworkState.RUNNING)
        ioScope.launch(getErrorHandler() + supervisorJob) {
            val token = repository.auth(email, secretNum)
            _networkState.postValue(NetworkState.SUCCESS)
            setProfile(token = token.accessToken, refresh = token.refreshToken)
            saveProfile()
            finishAuth.postValue(true)
        }
    }

    fun saveProfile(){
        sharedPrefsManager.saveProfile(profile)
    }

    private fun getErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.d("M__AuthModel", "An error happened: $e")
        e.message?.let {
            if (it.contains("HTTP 401") || it.contains("HTTP 403") || it.contains("HTTP 406")) {
                _networkState.postValue(NetworkState.WRONGDATA)
            } else _networkState.postValue(NetworkState.FAILED)
        } ?: _networkState.postValue(NetworkState.FAILED)

        supervisorJob.cancelChildren()
    }

    fun setProfile(
        name: String? = null,
        email: String? = null,
        token: String? = null,
        refresh: String? = null
    ) {
        name?.let { profile.name = name }
        email?.let { profile.email = email }
        refresh?.let { profile.refresh = refresh }
        token?.let { profile.token = token }
    }
}