package com.example.mymovieapp.view.search;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovieapp.R;
import com.example.mymovieapp.data.model.Movie;
import com.example.mymovieapp.data.model.MovieResponse;
import com.example.mymovieapp.data.network.RetrofitClient;
import com.example.mymovieapp.view.MyRecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements SearchAdapter.RecyclerViewClickListener
{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        RecyclerView myRecyclerView = rootView.findViewById(R.id.recyclerViewId);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final SearchAdapter searchAdapter = new SearchAdapter();
        searchAdapter.setOnRecyclerViewItemClickListener(this);
        myRecyclerView.setAdapter(searchAdapter);

        RetrofitClient retrofitClient = new RetrofitClient();

        final ImageView searchIcon = rootView.findViewById(R.id.searchImageViewId);
        final EditText searchEditText = rootView.findViewById(R.id.searchMovieEditTextId);
        final FloatingActionButton saveFab = rootView.findViewById(R.id.floatingActionBtnId);
        saveFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final List<Movie> checkedMovies = searchAdapter.getCurrentList().stream()
                        .filter(movie -> movie.isWatched())
                        .collect(Collectors.toList());

                if (checkedMovies.isEmpty())
                    Toast.makeText(getActivity(), "You don't have any checked movies to save", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Checked movies were saved!", Toast.LENGTH_SHORT).show();
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Call<MovieResponse> movies = retrofitClient.moviesApi().searchMovie(RetrofitClient.API_KEY, searchEditText.getText().toString());
                movies.enqueue(new Callback<MovieResponse>()
                {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response)
                    {
                        if (response.body() != null)
                        {
                            searchAdapter.submitList(response.body().getMovies());
                            Log.i("Fragment Search", "Movies: " + response.body().getMovies());
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t)
                    {
                        Log.e("Fragment Search", "Something happend", t);
                    }
                });
            }
        });

//        final Call<MovieResponse> movies = retrofitClient.moviesApi().getMovies(RetrofitClient.API_KEY);
//        movies.enqueue(new Callback<MovieResponse>()
//        {
//            @Override
//            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response)
//            {
//                if (response.body() != null)
//                    searchAdapter.submitList(response.body().getMovies());
//
//                Log.i("Fragment Search", "Movies: " + response.body().getMovies());
//            }
//
//            @Override
//            public void onFailure(Call<MovieResponse> call, Throwable t)
//            {
//                Log.e("Fragment Search", "Something happend", t);
//            }
//        });


        return rootView;
    }

    @Override
    public void onRecyclerViewItemClick(View view, Movie movie)
    {
        Toast.makeText(getActivity(), "Movie tapped: " + movie.toString(), Toast.LENGTH_SHORT).show();
    }
}
