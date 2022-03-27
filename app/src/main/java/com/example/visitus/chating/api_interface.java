package com.example.visitus.chating;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface api_interface {
    @GET
    Call<massageclass> call_message(@Url String url);
}
