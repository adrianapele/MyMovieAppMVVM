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
import com.example.mymovieapp.view.MyRecyclerView;
import com.squareup.picasso.Picasso;

public class FavoritesAdapter extends ListAdapter<Movie, FavoritesAdapter.FavoritesViewHolder>
{
    private RecyclerViewClickListener listener;

    public FavoritesAdapter()
    {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>()
    {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem)
        {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem)
        {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getOriginalTitle().equals(newItem.getOriginalTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getReleaseDate().equals(newItem.getReleaseDate());
        }
    };

    @NonNull
    @Override
    public FavoritesAdapter.FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View view = layoutInflater.inflate(R.layout.favorites_movie_list_row, null);
        return new FavoritesAdapter.FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.FavoritesViewHolder holder, int position)
    {
        Movie currentMovie = getItem(position);

        holder.movieTitleTextView.setText(currentMovie.getTitle());
        holder.movieYearTextView.setText(currentMovie.getReleaseDate());
        holder.movieNoteTextView.setText(currentMovie.getNote());
    }


    class FavoritesViewHolder extends MyRecyclerView.ViewHolder
    {
        TextView movieTitleTextView;
        TextView movieYearTextView;
        TextView movieNoteTextView;

        FavoritesViewHolder(@NonNull View itemView)
        {
            super(itemView);

            this.movieTitleTextView = itemView.findViewById(R.id.favoriteMovieTitleTextViewId);
            this.movieYearTextView = itemView.findViewById(R.id.favoriteMovieReleaseDateValueTextViewId);
            this.movieNoteTextView = itemView.findViewById(R.id.favoriteMovieNoteValueTextViewId);

            itemView.setOnClickListener(v ->
            {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION)
                    listener.onRecyclerViewItemClick(v, getItem(position));
            });
        }
    }

    public interface RecyclerViewClickListener
    {
        void onRecyclerViewItemClick(View view, Movie movie);
    }

    public void setOnRecyclerViewItemClickListener(FavoritesAdapter.RecyclerViewClickListener listener)
    {
        this.listener = listener;
    }
}
