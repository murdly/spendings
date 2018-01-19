package com.akarbowy.spendingsapp.managers;


import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private static final String PREDEFINED_DATA_LOADED = "PREDEFINED_DATA_LOADED";

    private final SharedPreferences preferences;

    public PreferencesManager(Context context) {
        this.preferences = context.getSharedPreferences("spending-prefs", Context.MODE_PRIVATE);
    }

    public void setPredefinedDataLoaded() {
        preferences.edit().putBoolean(PREDEFINED_DATA_LOADED, true).apply();
    }

    public boolean isPredefinedDataLoaded() {
        return preferences.getBoolean(PREDEFINED_DATA_LOADED, false);
    }
}
