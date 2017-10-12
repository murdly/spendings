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
    private final MutableLiveData<Period> period = new MutableLiveData<>();
    public LiveData<List<PeriodSpendings>> allByCurrency;

    public LiveData<PagedList<TransactionEntity>> transactions;

    public OverviewViewModel(final AppDatabase appDatabase) {
        transactions = appDatabase.transactionDao().allByTitle()
                .create(1, new PagedList.Config.Builder()
                .setPrefetchDistance(5)
                .setPageSize(10)
                .setInitialLoadSizeHint(10)
                .setEnablePlaceholders(true)
                .build());

        allByCurrency = Transformations.switchMap(period, new Function<Period, LiveData<List<PeriodSpendings>>>() {
            @Override public LiveData<List<PeriodSpendings>> apply(Period input) {
                return appDatabase.transactionDao().allByCurrency(input.from, input.to);
            }
        });
    }

    public void setPeriod(Period p) {
        this.period.setValue(p);
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
