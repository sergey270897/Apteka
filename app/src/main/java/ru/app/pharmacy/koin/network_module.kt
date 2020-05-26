package ru.app.pharmacy.koin

import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.app.pharmacy.BuildConfig
import ru.app.pharmacy.network.PharmacyAPI
import ru.app.pharmacy.network.HeaderInterceptor

val networkModule = module {
    factory {
        OkHttpClient().newBuilder()
            .addInterceptor(HeaderInterceptor(get()))
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(BuildConfig.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    factory { get<Retrofit>().create(PharmacyAPI::class.java) }
}