package com.example.mymovieapp.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymovieapp.data.model.Movie;

import java.util.List;

@Dao
public interface MovieDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("DELETE FROM movie")
    void deleteAllSavedMovies();

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAllSavedMovies();
}
