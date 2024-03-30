package com.example.sickpredict.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PredictionApiService {

    @POST("predict")
    Call<utils.PredictionResponse> getPrediction(@Body utils.PredictionRequest request);

    @POST("predict")
    Call<utils.MedicineResponse> getMedicinePrediction(@Body utils.MedicineRequest request);
}
