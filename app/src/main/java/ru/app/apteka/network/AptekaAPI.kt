package ru.app.apteka.network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import ru.app.apteka.models.AuthResponse
import ru.app.apteka.models.CategoryResponse
import ru.app.apteka.models.EmailResponse
import ru.app.apteka.models.MedicineResponse

interface AptekaAPI {
    @GET("/api/categories")
    fun getCategories(): Call<CategoryResponse>

    @GET("/api/search")
    fun searchMedicine(
        @Query("q") q: String,
        @Query("categoryId") categoryId: Int,
        @Query("priceFrom") priceFrom: Int,
        @Query("priceTo") priceTo: Int,
        @Query("available") available: Boolean,
        @Query("count") count: Int,
        @Query("offset") offset: Int,
        @Query("orderBy") orderBy: String,
        @Query("order") order: String
    ): Call<MedicineResponse>

    @Multipart
    @POST("/api/send_email")
    fun sendEmail(@Part("email") email:RequestBody):Call<EmailResponse>

    @Multipart
    @POST("/api/auth")
    fun auth(@Part("email") email: RequestBody, @Part("secretNum") secretNum:RequestBody):Call<AuthResponse>
}