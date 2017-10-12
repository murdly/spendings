package com.akarbowy.spendingsapp;


import android.app.Application;

import com.akarbowy.spendingsapp.data.AppDatabase;
import com.facebook.stetho.Stetho;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);

        AppDatabase appDatabase = AppDatabase.getInstance(this);
//
//        appDatabase.categoryDao().insert(PopulateUtil.createListOfCategories());
//        appDatabase.currencyDao().insert(PopulateUtil.createListOfCurrencies());
//        appDatabase.transactionDao().insert(PopulateUtil.createListOfTransactions());
    }
}
