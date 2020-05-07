package com.example.mymovieapp.data.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient
{
    static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String API_KEY = "26fde7d83066e38775d9c03328f2bba9";

    private final MovieApis moviesApi;

    public RetrofitClient()
    {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        this.moviesApi = retrofit.create(MovieApis.class);
    }

    public MovieApis moviesApi()
    {
        return moviesApi;
    }
}
