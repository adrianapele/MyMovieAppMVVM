package com.example.mymovieapp.view;

import android.app.Application;

import timber.log.Timber;

public class ApplicationController extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
