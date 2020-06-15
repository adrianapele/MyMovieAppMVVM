package com.example.mymovieapp.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.mymovieapp.R;
import com.example.mymovieapp.data.model.Movie;
import com.example.mymovieapp.view.fragments.favorites.FavoritesFragment;
import com.example.mymovieapp.view.fragments.home.HomeFragment;
import com.example.mymovieapp.view.fragments.search.SearchFragment;
import com.example.mymovieapp.viewmodel.FavoritesViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawerLayout;
    private FavoritesViewModel favoritesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();

            navigationView.setCheckedItem(R.id.nav_home);
        }

        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
                break;

            case R.id.nav_favorite:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new FavoritesFragment())
                        .commit();
                break;

            case R.id.nav_search:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new SearchFragment())
                        .commit();
                break;

            case R.id.nav_share:
                final List<Movie> allSavedMovies = favoritesViewModel.getSavedMovies().getValue();
                if (allSavedMovies == null || allSavedMovies.size() == 0)
                {
                    Toast.makeText(this, "You don't have saved movies to share", Toast.LENGTH_SHORT).show();
                    break;
                }

                final String savedMoviesToText = allSavedMovies
                        .stream()
                        .map(Movie::toText)
                        .collect(Collectors.joining(System.getProperty("line.separator")));

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:pele_adriana@yahoo.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "My Movie List");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi there! Checkout my favorite movie list: " + System.getProperty("line.separator") + savedMoviesToText);
                startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
