package com.example.mymovieapp.view.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovieapp.R;
import com.example.mymovieapp.data.model.Movie;
import com.example.mymovieapp.view.MyRecyclerView;

public class SearchAdapter extends ListAdapter<Movie, SearchAdapter.SearchViewHolder>
{
    private RecyclerViewClickListener listener;

    public SearchAdapter()
    {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>()
    {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem)
        {
//            return oldItem.getId() == newItem.getId();
            // maybe here check the title??
            return false;
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
        holder.movieYearTextView.setText(currentMovie.getReleaseDate());
        holder.movieNoteTextView.setText(currentMovie.getNote());
        holder.checkBoxButton.setChecked(currentMovie.isWatched());
    }


    class SearchViewHolder extends MyRecyclerView.ViewHolder
    {
        TextView movieTitleTextView;
        TextView movieYearTextView;
        TextView movieNoteTextView;
        CheckBox checkBoxButton;

        SearchViewHolder(@NonNull View itemView)
        {
            super(itemView);

            this.movieTitleTextView = itemView.findViewById(R.id.movieTitleTextViewId);
            this.movieYearTextView = itemView.findViewById(R.id.movieReleaseDateValueTextViewId);
            this.movieNoteTextView = itemView.findViewById(R.id.movieNoteValueTextViewId);
            this.checkBoxButton = itemView.findViewById(R.id.checkboxButtonId);

            this.checkBoxButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    final Movie movie = getItem(getAdapterPosition());
                    movie.setWatched(isChecked);
                }
            });

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

    public void setOnRecyclerViewItemClickListener(RecyclerViewClickListener listener)
    {
        this.listener = listener;
    }
}
