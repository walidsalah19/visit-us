package com.example.visitus.chating;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class retrofit {
    public static Retrofit callretrofit()
    {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://api.brainshop.ai/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

}
