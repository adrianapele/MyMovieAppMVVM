package com.example.mymovieapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mymovieapp.data.model.Movie;
import com.example.mymovieapp.data.repository.MovieRepository;

import java.util.List;

import timber.log.Timber;

public class FavoritesViewModel extends AndroidViewModel
{
    private MovieRepository movieRepository;
    private MutableLiveData<Movie> currentSelectedMovie = new MutableLiveData<>();

    private MediatorLiveData<List<Movie>> allSavedMovies = new MediatorLiveData<>();

    public FavoritesViewModel(Application application)
    {
        super(application);
        this.movieRepository = new MovieRepository(application);

        allSavedMovies.addSource(movieRepository.getAllSavedMovies(), movies ->
        {
            allSavedMovies.postValue(movies);
            Timber.i("saved movies in favorite view model %s", movies);
        });
    }

    public LiveData<Movie> getCurrentSelectedMovie()
    {
        return currentSelectedMovie;
    }

    public void setCurrentSelectedMovie(Movie movie)
    {
        this.currentSelectedMovie.setValue(movie);
    }

    public MediatorLiveData<List<Movie>> getSavedMovies()
    {
        return allSavedMovies;
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