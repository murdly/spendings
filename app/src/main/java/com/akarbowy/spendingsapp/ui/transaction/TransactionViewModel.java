package com.akarbowy.spendingsapp.ui.transaction;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.util.Log;

import com.akarbowy.spendingsapp.App;
import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.CurrencyDictionary;
import com.akarbowy.spendingsapp.data.entities.CategoryEntity;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by arek.karbowy on 20/10/2017.
 */

public class TransactionViewModel extends ViewModel {

    public final LiveData<Map<CategoryEntity, List<CategoryEntity>>> categoriesWithSubs;
    private AppDatabase appDatabase;
    private MutableLiveData<LocalDate> localDate = new MutableLiveData<>();
    private MutableLiveData<CurrencyDictionary> currency = new MutableLiveData<>();
    private MutableLiveData<CategoryEntity> category = new MutableLiveData<>();
    public TransactionViewModel(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;

        setLocalDate(LocalDate.now());
        setCurrency(App.DEFAULT_CURRENCY);

        categoriesWithSubs = Transformations.map(appDatabase.categoryDao().all()
                , new Function<List<CategoryEntity>, Map<CategoryEntity, List<CategoryEntity>>>() {
                    @Override
                    public Map<CategoryEntity, List<CategoryEntity>> apply(List<CategoryEntity> input) {
                        Map<Integer, CategoryEntity> categories = new HashMap<>();
                        for (CategoryEntity entity : input) {
                            if (entity.parentId == 0) {
                                categories.put(entity.id, entity);
                            }
                        }

                        Map<CategoryEntity, List<CategoryEntity>> result = new HashMap<>();
                        for (Map.Entry<Integer, CategoryEntity> category : categories.entrySet()) {
                            List<CategoryEntity> subCategories = new ArrayList<>();
                            for (CategoryEntity entity : input) {
                                if (category.getKey().equals(entity.parentId)) {
                                    subCategories.add(entity);
                                }
                            }
                            result.put(category.getValue(), subCategories);
                        }

                        return result;
                    }
                });
    }

    public final CurrencyDictionary[] getAvailableCurrencies() {
        return new CurrencyDictionary[]{
                CurrencyDictionary.GBP,
                CurrencyDictionary.USD,
                CurrencyDictionary.EUR,
                CurrencyDictionary.PLN
        };
    }

    public final LiveData<String> getFormattedDate() {
        return Transformations.map(localDate, new Function<LocalDate, String>() {
            @Override
            public String apply(LocalDate input) {
                String formatted = input.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                return formatted;
            }
        });
    }

    public LocalDate getLocalDate() {
        return localDate.getValue();
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate.setValue(localDate);
    }

    public MutableLiveData<CurrencyDictionary> getChosenCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyDictionary currency) {
        this.currency.setValue(currency);
    }

    public MutableLiveData<CategoryEntity> getChosenCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category.setValue(category);
    }

    public void saveTransaction() {
        TransactionEntity transaction = new TransactionEntity();
        transaction.categoryId = category.getValue().id;
        transaction.currencyId = currency.getValue().id;
        transaction.title = "z vm";
        transaction.value = 503;
        transaction.date = new Date();//localDate.getValue();
        Log.d("TransactionViewModel", transaction.toString());
        appDatabase.transactionDao().insertTransaction(transaction);
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final AppDatabase appDatabase;

        public Factory(AppDatabase appDatabase) {
            this.appDatabase = appDatabase;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(TransactionViewModel.class)) {
                return (T) new TransactionViewModel(appDatabase);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
