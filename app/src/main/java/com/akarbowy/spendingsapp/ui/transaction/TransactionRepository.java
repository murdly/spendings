package com.akarbowy.spendingsapp.ui.transaction;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.PagedList;
import android.os.AsyncTask;

import com.akarbowy.spendingsapp.App;
import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.entities.PeriodSpendingsData;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;
import com.akarbowy.spendingsapp.managers.PreferencesManager;
import com.akarbowy.spendingsapp.network.exchangerate.ExchangeRateResponse;
import com.akarbowy.spendingsapp.network.exchangerate.ExchangeRateService;
import com.akarbowy.spendingsapp.ui.SpendingsPeriod;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class TransactionRepository {

    private final AppDatabase database;

    private final PreferencesManager preferencesManager;

    private final ExchangeRateService.Api exchangeRateService;

    public TransactionRepository(AppDatabase database, PreferencesManager preferencesManager, ExchangeRateService.Api exchangeRateService) {
        this.database = database;
        this.preferencesManager = preferencesManager;
        this.exchangeRateService = exchangeRateService;
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
        final String baseCurrency = App.DC.isoCode;

        exchangeRateService.getExchangeRates(baseCurrency).enqueue(new Callback<ExchangeRateResponse>() {
            @Override public void onResponse(Call<ExchangeRateResponse> call, Response<ExchangeRateResponse> response) {
                if (response.isSuccessful()) {
                    preferencesManager.setLatestExchangeRates(response.body());

                    Timber.i("exchange rates success, %s", response.raw());
                }
            }

            @Override public void onFailure(Call<ExchangeRateResponse> call, Throwable t) {
                Timber.i(t, "exchange rates failure");
            }
        });

        return Transformations.switchMap(period, input -> {

            LiveData<List<PeriodSpendingsData>> result =
                    database.transactionDao().byCurrencyBetween(input.from(), input.to());

            final ExchangeRateResponse exchangeRate = preferencesManager.getLatestExchangeRates();
            if (exchangeRate != null && exchangeRate.getBase().equals(baseCurrency)) {

                result = Transformations.map(result, spendings -> {
                    for (PeriodSpendingsData spending : spendings) {
                        final double rate = exchangeRate.getRate(spending.isoCode);

                        spending.estimation = rate == 0 ? spending.total : spending.total / rate;
                    }

                    return spendings;
                });
            }

            return result;

            //TODO feature-1: merge with previous month to make comparision possible later on
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

    public void saveTransactions(List<TransactionEntity> transactions) {
        for (TransactionEntity transaction : transactions) {
            saveTransaction(transaction);
        }
    }

    public void saveTransaction(TransactionEntity transaction) {
        AsyncTask.execute(() -> database.transactionDao().insertTransaction(transaction));

        Timber.i("saveTransaction| %s", transaction.toString());
    }

    public void updateTransaction(TransactionEntity transaction) {
        //Deleted entries will be shown as disabled, so they should be updated instead
        final TransactionEntity copy = new TransactionEntity();

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
