package ru.app.pharmacy.viewmodels

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.json.JSONObject
import ru.app.pharmacy.models.AuthResponse
import ru.app.pharmacy.models.Profile
import ru.app.pharmacy.network.NetworkState
import ru.app.pharmacy.repositories.AuthRepository
import ru.app.pharmacy.repositories.manager.SharedPrefsManager
import ru.app.pharmacy.ui.base.BaseViewModel
import java.util.*

class AuthModel(
    private val repository: AuthRepository,
    private val sharedPrefsManager: SharedPrefsManager
) :
    BaseViewModel() {

    companion object{
        private const val MINUTE = 60000L
        private const val SECOND = 1000L
    }

    private var supervisorJob = SupervisorJob()

    private lateinit var timer: CountDownTimer
    private val _networkState = MutableLiveData<NetworkState>()
    val time = MutableLiveData(MINUTE / SECOND)
    val networkState: LiveData<NetworkState>
    val profile = Profile(null, null, null, null, null)
    val finishAuth = MutableLiveData(false)

    init {
        initTimer()
        networkState = _networkState
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        supervisorJob.cancelChildren()
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

    private fun stopTimer() {
        timer.cancel()
    }

    private fun saveProfile() {
        sharedPrefsManager.saveProfile(profile)
    }

    private fun getErrorHandler() = CoroutineExceptionHandler { _, e ->
        e.message?.let {
            if (it.contains("HTTP 401") || it.contains("HTTP 403") || it.contains("HTTP 406")) {
                val state = NetworkState.WRONG_DATA
                state.code = 0
                _networkState.postValue(state)
            } else _networkState.postValue(NetworkState.FAILED)
        } ?: _networkState.postValue(NetworkState.FAILED)
        supervisorJob.cancelChildren()
    }

    /*
    * role
    * 0 - admin
    * 1 - pharm
    * 2 - user
    *
    * only user can login
    * */
    private fun checkUserLevel(auth:AuthResponse):Boolean{
        var token = auth.accessToken.split(".")[1]
        token = String(Base64.getDecoder().decode(token))
        val level = JSONObject(token).getInt("level")
        return level == 2
    }

    fun startTimer() {
        time.value = MINUTE / SECOND
        timer.start()
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
            if(checkUserLevel(token)){
                _networkState.postValue(NetworkState.SUCCESS)
                setProfile(token = token.accessToken, refresh = token.refreshToken)
                saveProfile()
            }else{
                val state = NetworkState.WRONG_DATA
                state.code = 1
                _networkState.postValue(state)
            }
            finishAuth.postValue(true)
        }
    }

    fun setProfile(
        name: String? = null,
        email: String? = null,
        token: String? = null,
        refresh: String? = null,
        bd:String? = null
    ) {
        name?.let { profile.name = name }
        email?.let { profile.email = email }
        refresh?.let { profile.refresh = refresh }
        token?.let { profile.token = token }
        bd?.let { profile.bd = bd }
    }
}