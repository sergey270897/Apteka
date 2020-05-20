package ru.app.apteka.koin

import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.app.apteka.BuildConfig
import ru.app.apteka.network.AptekaAPI
import ru.app.apteka.network.HeaderInterceptor

val networkModule = module {
    factory { OkHttpClient().newBuilder().addInterceptor(HeaderInterceptor()).build() }
    single{
        Retrofit.Builder()
            .client(get())
            .baseUrl(BuildConfig.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    factory { get<Retrofit>().create(AptekaAPI::class.java) }
}