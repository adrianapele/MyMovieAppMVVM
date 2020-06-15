package com.example.mymovieapp.view.fragments.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovieapp.R;
import com.example.mymovieapp.data.model.Movie;
import com.example.mymovieapp.view.adapters.SearchAdapter;
import com.example.mymovieapp.viewmodel.SearchViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.stream.Collectors;

import timber.log.Timber;

public class SearchFragment extends Fragment
{
    public static final String TAG = "searchFragment";

    private SearchViewModel searchViewModel;
    private ProgressBar progressBar;
    private RecyclerView myRecyclerView;
    private SearchAdapter searchAdapter;
    private EditText searchEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        final ImageView searchIcon = rootView.findViewById(R.id.searchImageViewId);
        searchIcon.setOnClickListener(v -> searchMovie());
        searchEditText = rootView.findViewById(R.id.searchMovieEditTextId);

        myRecyclerView = rootView.findViewById(R.id.recyclerViewId);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setHasFixedSize(true);

        searchAdapter = new SearchAdapter();
        myRecyclerView.setAdapter(searchAdapter);

        final FloatingActionButton saveFab = rootView.findViewById(R.id.floatingActionBtnId);
        saveFab.setOnClickListener(v -> saveMovies());

        progressBar = rootView.findViewById(R.id.progressBarId);

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchRandomMovies();

        getActivity().setTitle("Search Movie");

        return rootView;
    }

    private void searchRandomMovies()
    {
        showLoading();
        searchViewModel.getMovies().observe(getViewLifecycleOwner(), movies ->
        {
            searchAdapter.submitList(movies);
            Timber.i("search random movies %s", movies);
            hideLoading();
        });
    }

    private void searchMovie()
    {
        showLoading();
        searchViewModel.searchMovies(searchEditText.getText().toString()).observe(getViewLifecycleOwner(), movies ->
        {
            searchAdapter.submitList(movies);
            Timber.i("search movies %s", movies);
            hideLoading();
        });
    }

    private void hideLoading()
    {
        progressBar.setVisibility(View.GONE);
        myRecyclerView.setEnabled(true);
    }

    private void showLoading()
    {
        progressBar.setVisibility(View.VISIBLE);
        myRecyclerView.setEnabled(false);
    }

    private void saveMovies()
    {
        final List<Movie> checkedMovies = searchAdapter.getCurrentList().stream()
                .filter(Movie::isWatched)
                .collect(Collectors.toList());

        if (checkedMovies.isEmpty())
            Toast.makeText(getActivity(), "You don't have any checked movies to save", Toast.LENGTH_SHORT).show();
        else
        {
            checkedMovies.forEach(movie -> searchViewModel.saveMovie(movie));
            Toast.makeText(getContext(), "Checked movies were saved!", Toast.LENGTH_SHORT).show();
        }
    }
}
