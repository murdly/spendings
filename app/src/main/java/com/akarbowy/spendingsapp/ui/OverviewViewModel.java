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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class OverviewViewModel extends ViewModel {
    public static PeriodType[] PERIODS = {
            PeriodType.THIS_MONTH,
            PeriodType.PREVIOUS_MONTH,
            PeriodType.ALL_TIME,
            PeriodType.CUSTOM
    };

    private final AppDatabase appDatabase;
    private final MutableLiveData<Period> period = new MutableLiveData<>();
    public final LiveData<List<PeriodSpendings>> periodicByCurrency;
    public final LiveData<PagedList<TransactionEntity>> transactions;
    private PeriodType periodType;

    public OverviewViewModel(final AppDatabase appDatabase) {
        this.appDatabase = appDatabase;

        this.setPeriod(0);

        transactions = this.appDatabase.transactionDao().allByTitle()
                .create(0, new PagedList.Config.Builder()
                        .setPageSize(20)
                        .setPrefetchDistance(20)
                        .setEnablePlaceholders(true)
                        .build());

        periodicByCurrency = Transformations.switchMap(period, new Function<Period, LiveData<List<PeriodSpendings>>>() {
            @Override
            public LiveData<List<PeriodSpendings>> apply(Period input) {
                if (input == null) {
                    return appDatabase.transactionDao().byCurrency();
                }

                return appDatabase.transactionDao().byCurrencyBetween(input.from, input.to);
            }
        });
    }

    public void setPeriod(int periodPosition) {
        periodType = PERIODS[periodPosition];
        Period range = null;
        Calendar now = Calendar.getInstance(TimeZone.getDefault());

        if (periodType == PeriodType.THIS_MONTH) {
            Calendar fromThisMonth = Calendar.getInstance();
            fromThisMonth.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.getActualMinimum(Calendar.DAY_OF_MONTH));
            Calendar toThisMonth = Calendar.getInstance();
            toThisMonth.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.getActualMaximum(Calendar.DAY_OF_MONTH));
            range = new OverviewViewModel.Period(fromThisMonth.getTime(), toThisMonth.getTime());
        } else if (periodType == PeriodType.PREVIOUS_MONTH) {
            range = new OverviewViewModel.Period(
                    new Date(2017, 9, 1), new Date(2017, 9, 30));
        } else if (periodType == PeriodType.CUSTOM) {
            range = new OverviewViewModel.Period(
                    new Date(2016, 10, 1), new Date(2017, 9, 10));
        } else {
            // all time
        }

        this.period.setValue(range);
    }

    public void onDeleteRecentTransaction(TransactionEntity item) {
        item.title = "DELETED";
        appDatabase.transactionDao().updateTransaction(item);
    }

    public String getSelectedPeriodTitle() {
        if (periodType != PeriodType.CUSTOM) {
            return periodType.title;
        }

        //title based on chosen period range. default: current month
        return "May, 11 2016 - Oct, 21 2017";
    }

    //TODO null when ALL_TIME
    public String getPeriodFromTitle() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);
        String fromTitle = sdf.format(period.getValue().from);
        return fromTitle;
    }

    public String getPeriodToTitle() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);
        String toTitle = sdf.format(period.getValue().to);
        return toTitle;
    }

    public enum PeriodType {
        THIS_MONTH("This month"),
        PREVIOUS_MONTH("Previous month"),
        ALL_TIME("All time"),
        CUSTOM("Custom");

        public final String title;

        PeriodType(String title) {
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

    public static class Factory implements ViewModelProvider.Factory {

        private final AppDatabase appDatabase;

        public Factory(AppDatabase appDatabase) {
            this.appDatabase = appDatabase;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(OverviewViewModel.class)) {
                return (T) new OverviewViewModel(appDatabase);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
