package com.example.sickpredict.api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Data class to represent the response from the API
data class PredictionResponse(
    @SerializedName("prediction")
    val prediction: String,
    @SerializedName("Accuracy")
    val accuracy: String
)

data class PredictionRequestBody(
    val symptoms: List<String>
)

data class MedicineRequestBody(
    val disease: String,
    val age: Int,
    val gender: String
)

data class MedicinePredictionResponse(val predicted_drugs: List<String>)


// Interface for API endpoints
interface ApiService {
    @POST("predict")
    fun getPrediction(@Body body: PredictionRequestBody): Call<PredictionResponse>

    @POST("predict")
    fun getMedicinePrediction(@Body body: MedicineRequestBody): Call<MedicinePredictionResponse>
}


// Retrofit client setup
object RetrofitClient {
    private const val BASE_URL = "https://ml1-wqn5.onrender.com/"
    private const val MEDICINE_BASE_URL = "https://drug-prediction-ghnj.onrender.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val medicineRetrofit = Retrofit.Builder()
        .baseUrl(MEDICINE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
    val medicineApiService: ApiService = medicineRetrofit.create(ApiService::class.java)
}

