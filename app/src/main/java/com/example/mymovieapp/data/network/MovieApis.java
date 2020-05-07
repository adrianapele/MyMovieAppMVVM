package com.example.mymovieapp.data.network;

import com.example.mymovieapp.data.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApis
{
    @GET("discover/movie")
    public Call<MovieResponse> getMovies(@Query("api_key") String api_key);

    @GET("search/movie")
    public Call<MovieResponse> searchMovie(@Query("api_key") String api_key, @Query("query") String query);
}
