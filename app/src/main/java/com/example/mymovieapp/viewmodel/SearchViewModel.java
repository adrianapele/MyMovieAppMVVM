package com.example.mymovieapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.mymovieapp.data.model.Movie;
import com.example.mymovieapp.data.repository.MovieRepository;

import java.util.List;

import timber.log.Timber;

public class SearchViewModel extends AndroidViewModel
{
    private MovieRepository movieRepository;

    private MediatorLiveData<List<Movie>> allMovies = new MediatorLiveData<>();

    public SearchViewModel(Application application)
    {
        super(application);
        this.movieRepository = new MovieRepository(application);

        allMovies.addSource(movieRepository.getAllMovies(), movies ->
        {
            allMovies.postValue(movies);
            Timber.i("all random movies in search view model %s", movies);
        });
    }

    public MediatorLiveData<List<Movie>> getMovies()
    {
        return allMovies;
    }

    public LiveData<List<Movie>> searchMovies(String query)
    {
        return movieRepository.searchMovies(query);
    }

    public void saveMovie(Movie movie)
    {
        movieRepository.insert(movie);
    }
}
