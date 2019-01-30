package com.example.yanec.onexbet;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {
    private static final RetrofitSingleton retrofit = new RetrofitSingleton();

    private APIServer server;

    public RetrofitSingleton() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIServer.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        server = retrofit.create(APIServer.class);
    }

    public static APIServer getInstance(){
        return retrofit.server;
    }
}
