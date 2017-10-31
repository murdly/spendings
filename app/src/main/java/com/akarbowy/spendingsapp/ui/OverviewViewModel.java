package com.akarbowy.spendingsapp.ui;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.paging.PagedList;

import com.akarbowy.spendingsapp.data.entities.PeriodSpendings;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;
import com.akarbowy.spendingsapp.ui.transaction.Transaction;
import com.akarbowy.spendingsapp.ui.transaction.TransactionRepository;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OverviewViewModel extends ViewModel {

    private static final SpendingsPeriod.Type[] PERIODS = SpendingsPeriod.Type.values();


    private final TransactionRepository repository;

    private SpendingsPeriod.Type type;
    private final MutableLiveData<SpendingsPeriod> period = new MutableLiveData<>();
    public final LiveData<List<PeriodSpendings>> periodicByCurrency;

    public final LiveData<PagedList<Transaction>> transactions;


    public OverviewViewModel(TransactionRepository repository) {
        this.repository = repository;

        this.setPeriod(0);

        transactions = repository.loadRecentTransactions();
        periodicByCurrency = repository.getExpensesInPeriod(period);
    }

    public SpendingsPeriod getPeriod() {
        return period.getValue();
    }

    public void setPeriod(int periodPosition) {
        this.type = PERIODS[periodPosition];

        SpendingsPeriod newPeriod;

        if (!isCustomPeriodSet()) {
            newPeriod = SpendingsPeriod.of(type);
        } else {
            SpendingsPeriod current = getPeriod();
            newPeriod = SpendingsPeriod.custom(current.from(), current.to());
        }
        
        this.period.setValue(newPeriod);
    }

    public void setCustomPeriod(LocalDateTime from, LocalDateTime to) {
        this.type = SpendingsPeriod.Type.CUSTOM;
        this.period.setValue(SpendingsPeriod.custom(from, to));
    }

    public boolean isCustomPeriodSet() {
        return type == SpendingsPeriod.Type.CUSTOM;
    }

    public String getSelectedPeriodTitle() {
        if (type != SpendingsPeriod.Type.CUSTOM) {
            return type.title;
        }

        return String.format("%s - %s", getPeriodFromTitle(), getPeriodToTitle());
    }

    public String getPeriodFromTitle() {
        LocalDate from = period.getValue().from().toLocalDate();
        return from.format(DateTimeFormatter.ofPattern("d MMM uuuu"));
    }

    public String getPeriodToTitle() {
        LocalDate to = period.getValue().to().toLocalDate();
        return to.format(DateTimeFormatter.ofPattern("d MMM uuuu"));
    }

    public void onDeleteTransaction(TransactionEntity item) {
        repository.updateTransaction(item);
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
