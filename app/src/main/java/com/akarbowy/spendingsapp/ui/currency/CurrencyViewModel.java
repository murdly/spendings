package com.akarbowy.spendingsapp.ui.currency;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;

import java.util.List;

public class CurrencyViewModel extends ViewModel {

    public final LiveData<List<CurrencyEntity>> currencies;

    private AppDatabase appDatabase;

    public CurrencyViewModel(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;

        currencies = appDatabase.currencyDao().all();
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final AppDatabase appDatabase;

        public Factory(AppDatabase appDatabase) {
            this.appDatabase = appDatabase;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(CurrencyViewModel.class)) {
                return (T) new CurrencyViewModel(appDatabase);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
