package ru.app.apteka.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONArray

import org.json.JSONObject

import org.json.JSONTokener

class HeaderInterceptor: Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("M__HeaderInterceptor",chain.request().url().toString())
        val response = chain.proceed(chain.request())
        Log.d("M__HeaderInterceptor",response.code().toString())

        val rawJson = response.body()?.string()
        if(response.code() == 200){
            val obj = JSONTokener(rawJson).nextValue()
            val jsonLog =
                if (obj is JSONObject) obj.toString(4) else (obj as JSONArray).toString(
                    4
                )
            Log.d("M__HeaderInterceptor",jsonLog)
        }

        return response.newBuilder()
            .body(ResponseBody.create(response.body()?.contentType(), rawJson)).build()
    }
}