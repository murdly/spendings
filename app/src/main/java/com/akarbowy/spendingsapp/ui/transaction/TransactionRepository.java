package com.akarbowy.spendingsapp.ui.transaction;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.PagedList;
import android.util.Log;

import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;
import com.akarbowy.spendingsapp.data.entities.GroupedCategories;
import com.akarbowy.spendingsapp.data.entities.PeriodSpendings;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;
import com.akarbowy.spendingsapp.ui.SpendingsPeriod;

import java.util.List;

/**
 * Created by arek.karbowy on 27/10/2017.
 */

public class TransactionRepository {
    private final AppDatabase database;

    public final LiveData<List<GroupedCategories>> categories;
    public final LiveData<List<CurrencyEntity>> currencies;

    public TransactionRepository(AppDatabase database) {
        this.database = database;

        categories = database.categoryDao().allGrouped();
        currencies = database.currencyDao().all();
    }

    public LiveData<List<CurrencyEntity>> getCurrencies() {
        return currencies;
    }

    public LiveData<List<GroupedCategories>> getCategories() {
        return categories;
    }

    public LiveData<Transaction> loadTransaction(int id) {
        return database.transactionDao().getById(id);
    }

    public LiveData<PagedList<Transaction>> loadRecentTransactions() {
        return database.transactionDao().allByNewestFirst()
                .create(0, new PagedList.Config.Builder()
                        .setPageSize(20)
                        .setPrefetchDistance(20)
                        .setEnablePlaceholders(true)
                        .build());
    }

    public LiveData<List<PeriodSpendings>> getExpensesInPeriod(MutableLiveData<SpendingsPeriod> period){
        return Transformations.switchMap(period, input -> {
            if (input == null) {
                return database.transactionDao().byCurrency();
            }

            return database.transactionDao().byCurrencyBetween(input.from(), input.to());
        });
    }

    public void saveTransaction(TransactionEntity transaction) {
        Log.d("REPO", "saveTransaction| " + transaction.toString());
        database.transactionDao().insertTransaction(transaction);
    }

    public void updateTransaction(TransactionEntity transaction) {
        //Deleted entries will be shown as disabled, so they should be updated instead
        TransactionEntity copy = new TransactionEntity();
        copy.transactionId = transaction.transactionId;
        copy.deleted = true;
        copy.title = transaction.title;
        copy.date = transaction.date;
        copy.value = transaction.value;
        copy.categoryId = transaction.categoryId;
        copy.currencyId = transaction.currencyId;

        saveTransaction(copy);
    }

}
