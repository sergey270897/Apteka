package ru.app.apteka.repositories

import okhttp3.MediaType
import okhttp3.RequestBody
import org.koin.core.context.KoinContextHandler.get
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import ru.app.apteka.BuildConfig
import ru.app.apteka.koin.networkModule
import ru.app.apteka.models.AuthResponse
import ru.app.apteka.models.Profile
import ru.app.apteka.network.AptekaAPI
import ru.app.apteka.repositories.manager.SharedPrefsManager

class TokenRepository(private val sharedPrefsManager: SharedPrefsManager){
    fun getProfile() = sharedPrefsManager.getProfile()

    fun refresh(refreshToken:String): Call<AuthResponse> {
        val rt = RequestBody.create(MediaType.parse("text/plain"), refreshToken)
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(AptekaAPI::class.java).refresh(rt)
    }

    fun saveProfile(profile: Profile){
        sharedPrefsManager.saveProfile(profile)
    }
}