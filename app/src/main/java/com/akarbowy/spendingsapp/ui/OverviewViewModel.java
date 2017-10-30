package com.akarbowy.spendingsapp.ui;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.paging.PagedList;

import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.entities.PeriodSpendings;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;
import com.akarbowy.spendingsapp.ui.transaction.Transaction;
import com.akarbowy.spendingsapp.ui.transaction.TransactionRepository;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

public class OverviewViewModel extends ViewModel {
    public static PeriodType[] PERIODS = {
            PeriodType.THIS_MONTH,
            PeriodType.PREVIOUS_MONTH,
            PeriodType.ALL_TIME,
            PeriodType.CUSTOM
    };

    private final TransactionRepository repository;
    private final MutableLiveData<Period> period = new MutableLiveData<>();
    public final LiveData<List<PeriodSpendings>> periodicByCurrency;
    public final LiveData<PagedList<Transaction>> transactions;
    private PeriodType periodType;

    public OverviewViewModel(TransactionRepository repository) {
        this.repository = repository;

        this.setPeriod(0);

        transactions = repository.loadRecentTransactions();

        periodicByCurrency = repository.getExpensesInPeriod(period);
    }

    public void setPeriod(int periodPosition) {
        periodType = PERIODS[periodPosition];
        Period range = null;
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalTime localTime = ZonedDateTime.now(ZoneOffset.UTC).with(LocalTime.MIDNIGHT).toLocalTime();
        LocalDate from = LocalDate.of(now.getYear(), now.getMonthValue(), 1);
        LocalDate to = LocalDate.of(now.getYear(), now.getMonthValue(), 31);
        if (periodType == PeriodType.THIS_MONTH) {

            from = LocalDate.of(now.getYear(), now.getMonthValue(), 1);
            to = LocalDate.of(now.getYear(), now.getMonthValue(), 31);
        } else if (periodType == PeriodType.PREVIOUS_MONTH) {
//            range = new OverviewViewModel.Period(
//                    LocalDate.of(2017, 9, 1), LocalDate.of(2017, 9, 30));
        } else if (periodType == PeriodType.CUSTOM) {
//            range = new OverviewViewModel.Period(
//                    LocalDate.of(2016, 10, 1), LocalDate.of(2017, 9, 10));
        } else {
            // all time
        }

        range = new OverviewViewModel.Period(LocalDateTime.of(from, localTime), LocalDateTime.of(to, localTime));

        this.period.setValue(range);
    }

    public void onDeleteTransaction(TransactionEntity item) {
        repository.updateTransaction(item);
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
        LocalDate from = period.getValue().from.toLocalDate();
        String fromTitle = from.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return fromTitle;
    }

    public String getPeriodToTitle() {
        LocalDate to = period.getValue().to.toLocalDate();
        String toTitle = to.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
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
        public LocalDateTime from;
        public LocalDateTime to;

        public Period(LocalDateTime from, LocalDateTime to) {
            this.from = from;
            this.to = to;
        }
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final TransactionRepository repository;

        public Factory(TransactionRepository repository) {
            this.repository = repository;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(OverviewViewModel.class)) {
                return (T) new OverviewViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
