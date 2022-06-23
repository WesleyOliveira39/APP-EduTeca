package com.example.edutecav1;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiEduteca {

    private static final String BASE_URL = "http://192.168.15.2/apiEduteca/";
    private static Retrofit retrofit;

    static Retrofit getApiEduteca(){

        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }

}
