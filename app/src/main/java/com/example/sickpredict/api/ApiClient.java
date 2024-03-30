package com.example.sickpredict.api;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL_1 = "https://ml1-9y6p.onrender.com/";
    private static final String BASE_URL_2 = "https://drug-prediction-ghnj.onrender.com/";
    private static Retrofit retrofit1 = null;
    private static Retrofit retrofit2 = null;

    public static Retrofit getClient(String baseUrl) {
        if (baseUrl.equals(BASE_URL_1)) {
            if (retrofit1 == null) {
                retrofit1 = new Retrofit.Builder()
                        .baseUrl(BASE_URL_1)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit1;
        } else if (baseUrl.equals(BASE_URL_2)) {
            if (retrofit2 == null) {
                retrofit2 = new Retrofit.Builder()
                        .baseUrl(BASE_URL_2)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit2;
        } else {
            throw new IllegalArgumentException("Invalid base URL: " + baseUrl);
        }
    }
}



