package com.example.mymovieapp.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovieapp.R;
import com.example.mymovieapp.data.model.Movie;
import com.example.mymovieapp.data.network.RetrofitClient;
import com.squareup.picasso.Picasso;

public class SearchAdapter extends ListAdapter<Movie, SearchAdapter.SearchViewHolder>
{
    public SearchAdapter()
    {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>()
    {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem)
        {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem)
        {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getReleaseDate().equals(newItem.getReleaseDate());
        }
    };

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View view = layoutInflater.inflate(R.layout.search_movie_list_row, null);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position)
    {
        Movie currentMovie = getItem(position);
        holder.movieTitleTextView.setText(currentMovie.getTitle());
        holder.movieDescTextView.setText(currentMovie.getDescription());
        holder.movieYearTextView.setText(currentMovie.getReleaseDate());
        holder.movieNoteTextView.setText(currentMovie.getNote());
        holder.checkBoxButton.setChecked(currentMovie.isWatched());

        Picasso.get()
                .load(RetrofitClient.IMAGE_URL + currentMovie.getPosterImagePath())
                .into(holder.movieImageView);
    }


    class SearchViewHolder extends RecyclerView.ViewHolder
    {
        TextView movieTitleTextView;
        TextView movieDescTextView;
        TextView movieYearTextView;
        TextView movieNoteTextView;
        ImageView movieImageView;
        CheckBox checkBoxButton;

        SearchViewHolder(@NonNull View itemView)
        {
            super(itemView);

            this.movieTitleTextView = itemView.findViewById(R.id.movieTitleTextViewId);
            this.movieDescTextView = itemView.findViewById(R.id.movieDescTextViewId);
            this.movieYearTextView = itemView.findViewById(R.id.movieReleaseDateValueTextViewId);
            this.movieNoteTextView = itemView.findViewById(R.id.movieNoteValueTextViewId);
            this.movieImageView = itemView.findViewById(R.id.movieImageViewId);
            this.checkBoxButton = itemView.findViewById(R.id.checkboxButtonId);

            this.checkBoxButton.setOnCheckedChangeListener((buttonView, isChecked) ->
            {
                final Movie movie = getItem(getAdapterPosition());
                movie.setWatched(isChecked);
            });
        }
    }
}
