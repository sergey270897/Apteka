package ru.app.pharmacy.network

import android.util.Log
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import ru.app.pharmacy.models.Profile
import ru.app.pharmacy.repositories.TokenRepository

class HeaderInterceptor(private val repository: TokenRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = setHeader(chain.request())
        val response = chain.proceed(request)

        Log.d("M__HeaderInterceptor", request.url().toString())
        Log.d("M__HeaderInterceptor", response.code().toString())
        val rawJson = response.body()?.string()

        if (response.code() == 200) printResponse(rawJson)

        if (response.code() == 406 && needToken(request.url())) {
            if (refreshToken() == 200) {
                val req = setHeader(chain.request())
                return chain.proceed(req)
            }
        }

        return response.newBuilder()
            .body(ResponseBody.create(response.body()?.contentType(), rawJson))
            .build()
    }

    private fun needToken(url: HttpUrl): Boolean {
        return when (url.encodedPath()) {
            "/api/order/add", "/api/me/change", "/api/order", "/api/me" -> true
            else -> false
        }
    }

    private fun setHeader(request: Request): Request {
        val profile = repository.getProfile()
        if (profile.token != null && needToken(request.url()))
            return request.newBuilder()
                .removeHeader("accessToken")
                .addHeader("accessToken", profile.token)
                .build()
        return request
    }

    private fun printResponse(rawJson: String?) {
        val obj = JSONTokener(rawJson).nextValue()
        val jsonLog =
            if (obj is JSONObject) obj.toString(4)
            else (obj as JSONArray).toString(4)
        Log.d("M__HeaderInterceptor", jsonLog)
    }

    private fun refreshToken(): Int {
        val profile = repository.getProfile()
        val refreshCall = repository.refresh(profile.refresh!!)
        val refreshResponse = refreshCall.execute()
        if (refreshResponse.code() == 200) {
            profile.token = refreshResponse.body()?.accessToken
            profile.refresh = refreshResponse.body()?.refreshToken
            repository.saveProfile(profile)
        } else {
            repository.saveProfile(Profile(null, null, null, null, null))
        }
        return refreshResponse.code()
    }
}