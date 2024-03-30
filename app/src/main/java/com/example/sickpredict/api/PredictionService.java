package com.example.sickpredict.api;

import java.util.List;

import retrofit2.Callback;

public class PredictionService {

    private PredictionApiService apiService1;
    private PredictionApiService apiService2;

    public PredictionService() {
        apiService1 = ApiClient.getClient("https://ml1-9y6p.onrender.com/").create(PredictionApiService.class);
        apiService2 = ApiClient.getClient("https://drug-prediction-ghnj.onrender.com/").create(PredictionApiService.class);
    }

    public void getPrediction(List<String> symptoms, Callback<utils.PredictionResponse> callback) {
        utils.PredictionRequest request = new utils.PredictionRequest(symptoms);
        apiService1.getPrediction(request).enqueue(callback);
    }

    public void getMedicinePrediction(String disease, int age, String gender, Callback<utils.MedicineResponse> callback) {
        utils.MedicineRequest request = new utils.MedicineRequest(disease, age, gender);
        apiService2.getMedicinePrediction(request).enqueue(callback);
    }
}

