package com.example.mymovieapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mymovieapp.data.model.Movie;
import com.example.mymovieapp.data.repository.MovieRepository;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel
{
    private MovieRepository movieRepository;
    private MutableLiveData<Movie> currentSelectedMovie = new MutableLiveData<>();

    public FavoritesViewModel(Application application)
    {
        super(application);
        this.movieRepository = new MovieRepository(application);
    }

    public LiveData<Movie> getCurrentSelectedMovie()
    {
        return currentSelectedMovie;
    }

    public void setCurrentSelectedMovie(Movie movie)
    {
        this.currentSelectedMovie.setValue(movie);
    }

    public LiveData<List<Movie>> getSavedMovies()
    {
        return movieRepository.getAllSavedMovies();
    }

    public void deleteMovie(Movie movie)
    {
        movieRepository.delete(movie);
    }

    public void deleteAllMovies()
    {
        movieRepository.deleteAllSavedMovies();
    }
}