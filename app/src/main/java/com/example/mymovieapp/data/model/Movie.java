package com.example.mymovieapp.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Movie
{
    @Expose
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("original_title")
    private String originalTitle;

    @Expose
    @SerializedName("overview")
    private String description;

    @Expose
    @SerializedName("release_date")
    private String releaseDate;

    @Expose
    @SerializedName("original_language")
    private String language;

    @Expose
    @SerializedName("vote_average")
    private String note;

    @Expose
    @SerializedName("vote_count")
    private String nrOfVotes;

    @Expose
    @SerializedName("poster_path")
    private String posterImagePath;

    @Expose
    @SerializedName("backdrop_path")
    private String backdropImagePath;

    private boolean watched;

    public Movie(int id, String title, String originalTitle, String description, String releaseDate, String language, String note, String nrOfVotes, String posterImagePath, String backdropImagePath)
    {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.description = description;
        this.releaseDate = releaseDate;
        this.language = language;
        this.note = note;
        this.nrOfVotes = nrOfVotes;
        this.posterImagePath = posterImagePath;
        this.backdropImagePath = backdropImagePath;
    }

    public int getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getOriginalTitle()
    {
        return originalTitle;
    }

    public String getDescription()
    {
        return description;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public String getLanguage()
    {
        return language;
    }

    public String getNote()
    {
        return note;
    }

    public String getNrOfVotes()
    {
        return nrOfVotes;
    }

    public String getPosterImagePath()
    {
        return posterImagePath;
    }

    public String getBackdropImagePath()
    {
        return backdropImagePath;
    }

    public void setWatched(boolean watched)
    {
        this.watched = watched;
    }

    public boolean isWatched()
    {
        return watched;
    }

    public String toText()
    {
        return "Title: " +  title + ", Release date: " + releaseDate + ", Note: " + note;
    }
}