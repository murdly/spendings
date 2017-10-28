package com.akarbowy.spendingsapp.ui.transaction;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.PagedList;

import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;
import com.akarbowy.spendingsapp.data.entities.GroupedCategories;
import com.akarbowy.spendingsapp.data.entities.PeriodSpendings;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;
import com.akarbowy.spendingsapp.ui.OverviewViewModel;

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

    public LiveData<PagedList<TransactionEntity>> loadRecentTransactions() {
        return database.transactionDao().allByTitle()
                .create(0, new PagedList.Config.Builder()
                        .setPageSize(20)
                        .setPrefetchDistance(20)
                        .setEnablePlaceholders(true)
                        .build());
    }

    public LiveData<List<PeriodSpendings>> getExpensesInPeriod(MutableLiveData<OverviewViewModel.Period> period){
        return Transformations.switchMap(period, input -> {
            if (input == null) {
                return database.transactionDao().byCurrency();
            }

            return database.transactionDao().byCurrencyBetween(input.from, input.to);
        });
    }

    public void saveTransaction(TransactionEntity transaction) {
        database.transactionDao().insertTransaction(transaction);
    }

    public void deleteTransaction(TransactionEntity transaction) {
        //Deleted entries will be shown as disabled, so they should be updated instead
        database.transactionDao().insertTransaction(transaction);
    }

}
