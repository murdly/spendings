package com.akarbowy.spendingsapp.ui;


import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.paging.PagedList;

import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.entities.PeriodSpendings;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;

import java.util.Date;
import java.util.List;

public class OverviewViewModel extends ViewModel {
    public static Periodd[] PERIODS = {
            Periodd.THIS_MONTH,
            Periodd.PREVIOUS_MONTH,
            Periodd.ALL_TIME,
            Periodd.CUSTOM
    };
    private final MutableLiveData<Period> period = new MutableLiveData<>();
    private final AppDatabase appDatabase;
    public LiveData<List<PeriodSpendings>> periodicByCurrency;
    public LiveData<PagedList<TransactionEntity>> transactions;

    public OverviewViewModel(final AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        transactions = this.appDatabase.transactionDao().allByTitle()
                .create(0, new PagedList.Config.Builder()
                        .setPageSize(20)
                        .setPrefetchDistance(20)
                        .setEnablePlaceholders(true)
                        .build());

        periodicByCurrency = Transformations.switchMap(period, new Function<Period, LiveData<List<PeriodSpendings>>>() {
            @Override public LiveData<List<PeriodSpendings>> apply(Period input) {
                if (input == null) {
                    return appDatabase.transactionDao().byCurrency();
                }

                return appDatabase.transactionDao().byCurrencyBetween(input.from, input.to);
            }
        });
    }

    public void setPeriod(int periodPosition) {
        Periodd p = PERIODS[periodPosition];
        Period range = null;

        if (p == Periodd.THIS_MONTH) {
            range = new OverviewViewModel.Period(
                    new Date(2017, 10, 1), new Date(2017, 10, 31));
        } else if (p == Periodd.PREVIOUS_MONTH) {
            range = new OverviewViewModel.Period(
                    new Date(2017, 9, 1), new Date(2017, 9, 30));
        } else if (p == Periodd.CUSTOM) {
            range = new OverviewViewModel.Period(
                    new Date(2016, 10, 1), new Date(2017, 9, 10));
        }

        this.period.setValue(range);
    }

    public void onDeleteRecentTransaction(TransactionEntity item) {
        item.title = "DELETED";
        appDatabase.transactionDao().updateTransaction(item);
    }

    public enum Periodd {
        THIS_MONTH("This month"),
        PREVIOUS_MONTH("Previous month"),
        ALL_TIME("All time"),
        CUSTOM("Custom");

        public final String title;

        Periodd(String title) {
            this.title = title;
        }
    }

    public static class Period {
        public Date from;
        public Date to;

        public Period(Date from, Date to) {
            this.from = from;
            this.to = to;
        }
    }

    public static class OVMFactory implements ViewModelProvider.Factory {

        private final AppDatabase appDatabase;

        public OVMFactory(AppDatabase appDatabase) {
            this.appDatabase = appDatabase;
        }

        @Override public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(OverviewViewModel.class)) {
                return (T) new OverviewViewModel(appDatabase);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
