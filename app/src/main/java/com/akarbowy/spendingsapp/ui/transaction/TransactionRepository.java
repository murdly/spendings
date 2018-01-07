package com.akarbowy.spendingsapp.ui.transaction;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.PagedList;
import android.util.Log;

import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;
import com.akarbowy.spendingsapp.data.entities.GroupedCategories;
import com.akarbowy.spendingsapp.data.entities.PeriodSpendingsData;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;
import com.akarbowy.spendingsapp.ui.SpendingsPeriod;

import java.util.List;

/**
 * Created by arek.karbowy on 27/10/2017.
 */

public class TransactionRepository {
    public final LiveData<List<GroupedCategories>> categories;
    public final LiveData<List<CurrencyEntity>> currencies;
    private final AppDatabase database;

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

    public LiveData<List<PeriodSpendingsData>> getExpensesInPeriod(MutableLiveData<SpendingsPeriod> period, SpendingsPeriod.Type type) {
        return Transformations.switchMap(period, input -> {
            LiveData<List<PeriodSpendingsData>> result = database.transactionDao().byCurrencyBetween(input.from(), input.to());
            return result;

            //TODO merge with previous month to make comparision possible later on
            /*if(type != SpendingsPeriod.Type.THIS_MONTH){
                return result;
            }else {
                SpendingsPeriod prevMonth = SpendingsPeriod.of(SpendingsPeriod.Type.PREVIOUS_MONTH);
                LiveData<List<PeriodSpendingsData>> prevMonthData =
                        database.transactionDao().byCurrencyBetween(prevMonth.from(), prevMonth.to());

                MediatorLiveData<List<PeriodSpendingsData>> merged = new MediatorLiveData<>();
                merged.addSource(result, v -> merged.setValue(v));
                merged.addSource(prevMonthData, v -> merged.setValue(v));
                return merged;
            }*/
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
