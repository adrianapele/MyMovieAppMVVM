package com.example.mymovieapp.view.fragments.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.List;

import timber.log.Timber;

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

        favoritesViewModel = new ViewModelProvider(getActivity()).get(FavoritesViewModel.class);
        favoritesViewModel.getSavedMovies().observe(getViewLifecycleOwner(), movies ->
        {
            adapter.submitList(movies);
            Timber.i("Saved movies in fragment %s", movies);
        });

        getActivity().setTitle("Favorites");

        return rootView;
    }

    @Override
    public void onRecyclerViewItemClick(View view, Movie movie)
    {
        favoritesViewModel.setCurrentSelectedMovie(movie);

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

        Timber.i("Current selected movie %s", movie.getTitle());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        inflater.inflate(R.menu.delete_all_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.delete_all_favorites_movie)
        {
            final List<Movie> allSavedMovies = favoritesViewModel.getSavedMovies().getValue();
            if (allSavedMovies == null || allSavedMovies.size() == 0 )
                Toast.makeText(getContext(), "You don't have saved movies to delete", Toast.LENGTH_SHORT).show();
            else
            {
                favoritesViewModel.deleteAllMovies();
                Toast.makeText(getContext(), "All saved movies were deleted", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
