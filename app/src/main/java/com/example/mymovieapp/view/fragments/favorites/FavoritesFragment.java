package com.example.mymovieapp.view.fragments.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovieapp.R;
import com.example.mymovieapp.data.model.Movie;
import com.example.mymovieapp.view.adapters.FavoritesAdapter;
import com.example.mymovieapp.view.fragments.details.DetailsFragment;
import com.example.mymovieapp.viewmodel.FavoritesViewModel;

public class FavoritesFragment extends Fragment implements FavoritesAdapter.RecyclerViewClickListener
{
    public static final String TAG = "favoritesFragment";

    private FavoritesViewModel favoritesViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.favoritesRecyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        FavoritesAdapter adapter = new FavoritesAdapter();
        adapter.setOnRecyclerViewItemClickListener(this);
        recyclerView.setAdapter(adapter);

        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
        favoritesViewModel.getSavedMovies().observe(getViewLifecycleOwner(), adapter::submitList);

        getActivity().setTitle("Favorites");

        return rootView;
    }

    @Override
    public void onRecyclerViewItemClick(View view, Movie movie)
    {
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

        favoritesViewModel.setCurrentSelectedMovie(movie);
    }
}
