package com.example.mymovieapp.view.fragments.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovieapp.R;
import com.example.mymovieapp.data.model.Movie;
import com.example.mymovieapp.view.adapters.SearchAdapter;
import com.example.mymovieapp.view.fragments.details.DetailsFragment;
import com.example.mymovieapp.viewmodel.SearchViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.stream.Collectors;

public class SearchFragment extends Fragment implements SearchAdapter.RecyclerViewClickListener
{
    private SearchViewModel searchViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        RecyclerView myRecyclerView = rootView.findViewById(R.id.recyclerViewId);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final SearchAdapter searchAdapter = new SearchAdapter();
        searchAdapter.setOnRecyclerViewItemClickListener(this);
        myRecyclerView.setAdapter(searchAdapter);

        searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);

        final ImageView searchIcon = rootView.findViewById(R.id.searchImageViewId);
        final EditText searchEditText = rootView.findViewById(R.id.searchMovieEditTextId);
        final FloatingActionButton saveFab = rootView.findViewById(R.id.floatingActionBtnId);
        saveFab.setOnClickListener(v ->
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
        });

        searchIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final LiveData<List<Movie>> movieListLiveData = searchViewModel.searchMovies(searchEditText.getText().toString());
                movieListLiveData.observe(getViewLifecycleOwner(), searchAdapter::submitList);
            }
        });

        return rootView;
    }

    @Override
    public void onRecyclerViewItemClick(View view, Movie movie)
    {
        searchViewModel.setCurrentSelectedMovie(movie);
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment detailsFragment = fragmentManager.findFragmentByTag(DetailsFragment.DETAILS_FRAGMENT_TAG);

        if (detailsFragment == null)
            detailsFragment = new DetailsFragment();

        fragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.fragment_container, detailsFragment, DetailsFragment.DETAILS_FRAGMENT_TAG)
                .addToBackStack(DetailsFragment.DETAILS_FRAGMENT_TAG)
                .commit();
    }
}
