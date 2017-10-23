package com.akarbowy.spendingsapp;


import android.app.Application;

import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.CurrencyDictionary;
import com.akarbowy.spendingsapp.data.PopulateUtil;
import com.akarbowy.spendingsapp.ui.transaction.TransactionViewModel;
import com.facebook.stetho.Stetho;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class App extends Application {

    public static CurrencyDictionary DEFAULT_CURRENCY = CurrencyDictionary.PLN;

    @Override public void onCreate() {
        super.onCreate();

        AndroidThreeTen.init(this);
        Stetho.initializeWithDefaults(this);

        AppDatabase appDatabase = AppDatabase.getInstance(this);
//
//        appDatabase.categoryDao().insert(PopulateUtil.createListOfCategories());
//        appDatabase.currencyDao().insert(PopulateUtil.createListOfCurrencies());
//        appDatabase.transactionDao().insert(PopulateUtil.createListOfTransactions());
    }
}
