package com.example.mymovieapp.data.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mymovieapp.data.database.MovieDao;
import com.example.mymovieapp.data.database.MovieDatabase;
import com.example.mymovieapp.data.model.Movie;
import com.example.mymovieapp.data.model.MovieResponse;
import com.example.mymovieapp.data.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository
{
    private MovieDao movieDao;
    private RetrofitClient retrofitClient;

    public MovieRepository(Application application)
    {
        MovieDatabase database = MovieDatabase.getInstance(application);
        movieDao = database.movieDao();
        retrofitClient = new RetrofitClient();
    }

    public void insert(Movie movie)
    {
        new InsertMovieAsyncTask(movieDao).execute(movie);
    }

    public void update(Movie movie)
    {
        new InsertMovieAsyncTask(movieDao).execute(movie);
    }

    public void delete(Movie movie)
    {
        new DeleteMovieAsyncTask(movieDao).execute(movie);
    }

    public void deleteAllSavedMovies()
    {
        new DeleteAllSavedMovieAsyncTask(movieDao).execute();
    }

    public LiveData<List<Movie>> getAllSavedMovies()
    {
        return movieDao.getAllSavedMovies();
    }

    public LiveData<List<Movie>> searchMovies(String query)
    {
        final MutableLiveData<List<Movie>> data = new MutableLiveData<>();

        retrofitClient.moviesApi()
                .searchMovie(RetrofitClient.API_KEY, query)
                .enqueue(new Callback<MovieResponse>()
                {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response)
                    {
                        final MovieResponse movieResponse = response.body();

                        if (movieResponse != null)
                        {
                            data.setValue(movieResponse.getMovies());
                            Log.i("MovieResponse", "#searchMovies: Successfully searched movies");
                        }
                        else
                        {
                            data.setValue(null);
                            Log.e("MovieRepository", "#searchMovies: Could not search for movies");
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t)
                    {
                        data.setValue(null);
                        Log.e("MovieRepository", "#searchMovies: Could not search for movies", t);
                    }
                });

        return data;
    }

    public LiveData<List<Movie>> getAllMovies()
    {
        final MutableLiveData<List<Movie>> data = new MutableLiveData<>();

        retrofitClient.moviesApi()
                .getMovies(RetrofitClient.API_KEY)
                .enqueue(new Callback<MovieResponse>()
                {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response)
                    {
                        final MovieResponse movieResponse = response.body();

                        if (movieResponse != null)
                        {
                            data.setValue(movieResponse.getMovies());
                            Log.i("MovieResponse", "#getAllMovies: Successfully got all movies");
                        }
                        else
                        {
                            data.setValue(null);
                            Log.e("MovieRepository", "#getAllMovies: Could not get all movies");
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t)
                    {
                        data.setValue(null);
                        Log.e("MovieRepository", "#getAllMovies: Could not get all movies", t);
                    }
                });

        return data;
    }


    private static class InsertMovieAsyncTask extends AsyncTask<Movie, Void, Void>
    {
        private MovieDao movieDao;

        private InsertMovieAsyncTask(MovieDao movieDao)
        {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies)
        {
            movieDao.insert(movies[0]);
            return null;
        }
    }

    private static class UpdateMovieAsyncTask extends AsyncTask<Movie, Void, Void>
    {
        private MovieDao movieDao;

        private UpdateMovieAsyncTask(MovieDao movieDao)
        {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies)
        {
            movieDao.update(movies[0]);
            return null;
        }
    }

    private static class DeleteMovieAsyncTask extends AsyncTask<Movie, Void, Void>
    {
        private MovieDao movieDao;

        private DeleteMovieAsyncTask(MovieDao movieDao)
        {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies)
        {
            movieDao.delete(movies[0]);
            return null;
        }
    }

    private static class DeleteAllSavedMovieAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private MovieDao movieDao;

        private DeleteAllSavedMovieAsyncTask(MovieDao movieDao)
        {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            movieDao.deleteAllSavedMovies();
            return null;
        }
    }
}
