package com.example.mymovieapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse
{
    @Expose
    @SerializedName("total_pages")
    private int nrOfPages;

    @Expose
    @SerializedName("total_results")
    private int totalResults;

    @Expose
    @SerializedName("page")
    private int currentPage;

    @Expose
    @SerializedName("results")
    private List<Movie> movies;

    public MovieResponse(int nrOfPages, int totalResults, int currentPage, List<Movie> movies)
    {
        this.nrOfPages = nrOfPages;
        this.totalResults = totalResults;
        this.currentPage = currentPage;
        this.movies = movies;
    }

    public int getNrOfPages()
    {
        return nrOfPages;
    }

    public int getTotalResults()
    {
        return totalResults;
    }

    public int getCurrentPage()
    {
        return currentPage;
    }

    public List<Movie> getMovies()
    {
        return movies;
    }
}
