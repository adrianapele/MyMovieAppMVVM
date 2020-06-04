package com.example.mymovieapp.data.network;

import com.example.mymovieapp.data.model.MovieResponse;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient
{
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500/";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "26fde7d83066e38775d9c03328f2bba9";

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

    public Call<MovieResponse> searchMovie(String query)
    {
        return moviesApi.searchMovie(API_KEY, query);
    }

    public Call<MovieResponse> getMovies()
    {
        return moviesApi.getMovies(API_KEY);
    }
}
