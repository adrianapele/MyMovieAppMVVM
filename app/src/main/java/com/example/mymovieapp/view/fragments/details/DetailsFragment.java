package com.example.mymovieapp.view.fragments.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mymovieapp.R;
import com.example.mymovieapp.data.model.Movie;
import com.example.mymovieapp.data.network.RetrofitClient;
import com.example.mymovieapp.viewmodel.FavoritesViewModel;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

public class DetailsFragment extends Fragment
{
    public static final String DETAILS_FRAGMENT_TAG = "detailsFragmentTag";

    private FavoritesViewModel favoritesViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        TextView titleTextView = rootView.findViewById(R.id.details_title_id);
        TextView descriptionTextView = rootView.findViewById(R.id.details_description_id);
        TextView releaseDateTextView = rootView.findViewById(R.id.details_release_date_value_id);
        TextView noteTextView = rootView.findViewById(R.id.details_note_value_id);
        ImageView movieImageView = rootView.findViewById(R.id.details_image_id);
        Button deleteButton = rootView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Movie currentMovie = favoritesViewModel.getCurrentSelectedMovie().getValue();
                favoritesViewModel.deleteMovie(currentMovie);
                Toast.makeText(getContext(), "Movie successfully deleted", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
                favoritesViewModel.setCurrentSelectedMovie(null);
            }
        });

        favoritesViewModel = new ViewModelProvider(getActivity()).get(FavoritesViewModel.class);
        favoritesViewModel.getCurrentSelectedMovie().observe(getViewLifecycleOwner(), currentMovie ->
        {
            if (currentMovie != null)
            {
                Timber.i("Current selected movie in details fragment %s", currentMovie);
                titleTextView.setText(currentMovie.getTitle());
                descriptionTextView.setText(currentMovie.getDescription());
                releaseDateTextView.setText(currentMovie.getReleaseDate());
                noteTextView.setText(currentMovie.getNote());

                Picasso.get().load(RetrofitClient.IMAGE_URL + currentMovie.getPosterImagePath()).into(movieImageView);
            }
        });

        getActivity().setTitle("Movie Details");

        return rootView;
    }
}
