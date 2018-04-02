package com.akarbowy.spendingsapp.managers;


import android.content.Context;
import android.content.SharedPreferences;

import com.akarbowy.spendingsapp.network.exchangerate.ExchangeRateResponse;
import com.google.gson.Gson;

import javax.annotation.Nullable;

public class PreferencesManager {

    private static final String PREDEFINED_DATA_LOADED = "PREDEFINED_DATA_LOADED";

    private static final String LATEST_EXCHANGE_RATES = "LATEST_EXCHANGE_RATES";

    private final Gson gson;

    private final SharedPreferences preferences;

    public PreferencesManager(Context context, Gson gson) {
        this.gson = gson;
        this.preferences = context.getSharedPreferences("spending-prefs", Context.MODE_PRIVATE);
    }

    public void setPredefinedDataLoaded() {
        preferences.edit().putBoolean(PREDEFINED_DATA_LOADED, true).apply();
    }

    public boolean isPredefinedDataLoaded() {
        return preferences.getBoolean(PREDEFINED_DATA_LOADED, false);
    }

    @Nullable
    public ExchangeRateResponse getLatestExchangeRates() {
        final String ratesJson = preferences.getString(LATEST_EXCHANGE_RATES, null);
        return ratesJson == null ? null : gson.fromJson(ratesJson, ExchangeRateResponse.class);
    }

    public void setLatestExchangeRates(ExchangeRateResponse rates) {
        final String ratesJson = gson.toJson(rates);
        preferences.edit().putString(LATEST_EXCHANGE_RATES, ratesJson).apply();
    }
}
