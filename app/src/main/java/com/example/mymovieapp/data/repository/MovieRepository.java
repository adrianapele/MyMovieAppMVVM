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
import timber.log.Timber;

public class MovieRepository
{
    private MovieDao movieDao;
    private RetrofitClient retrofitClient;

    private LiveData<List<Movie>> allSavedMovies;

    public MovieRepository(Application application)
    {
        MovieDatabase database = MovieDatabase.getInstance(application);
        this.movieDao = database.movieDao();
        this.retrofitClient = new RetrofitClient();

        this.allSavedMovies = movieDao.getAllSavedMovies();
        Timber.i("All saved movies in repo %s", this.allSavedMovies.getValue());
    }

    public void insert(Movie movie)
    {
        new InsertMovieAsyncTask(movieDao).execute(movie);
    }

    public void update(Movie movie)
    {
        new UpdateMovieAsyncTask(movieDao).execute(movie);
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
        return allSavedMovies;
    }

    public LiveData<List<Movie>> searchMovies(String query)
    {
        final MutableLiveData<List<Movie>> data = new MutableLiveData<>();

        retrofitClient
                .searchMovie(query)
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

        retrofitClient
                .getMovies()
                .enqueue(new Callback<MovieResponse>()
                {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response)
                    {
                        final MovieResponse movieResponse = response.body();

                        if (movieResponse != null)
                        {
                            data.setValue(movieResponse.getMovies());
                            Timber.i("#getAllMovies: Successfully got all movies");
                        }
                        else
                        {
                            data.setValue(null);
                            Timber.e("#getAllMovies: Could not get all movies");
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t)
                    {
                        data.setValue(null);
                        Timber.e(t, "#getAllMovies: Could not get all movies");
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
