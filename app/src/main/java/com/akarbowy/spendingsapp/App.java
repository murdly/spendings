package com.akarbowy.spendingsapp;


import android.app.Application;
import android.os.AsyncTask;

import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.Dictionaries;
import com.akarbowy.spendingsapp.data.PredefinedData;
import com.akarbowy.spendingsapp.managers.PreferencesManager;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.jakewharton.threetenabp.AndroidThreeTen;

import timber.log.Timber;


public class App extends Application {

    public static Dictionaries.Currency DC = Dictionaries.Currency.GBP;

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidThreeTen.init(this);

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
            Timber.plant(new Timber.DebugTree());
        }

        final AppDatabase appDatabase = AppDatabase.getInstance(this);

        final PreferencesManager preferencesManager = new PreferencesManager(getApplicationContext(), new Gson());

        if (!preferencesManager.isPredefinedDataLoaded()) {
            AsyncTask.execute(() -> {
                appDatabase.categoryDao().insert(PredefinedData.getCategories());
                appDatabase.currencyDao().insert(PredefinedData.getCurrencies());
                preferencesManager.setPredefinedDataLoaded();
            });
        }

    }
}
