package com.akarbowy.spendingsapp.ui.transaction;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.akarbowy.spendingsapp.App;
import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;
import com.akarbowy.spendingsapp.data.entities.CategoryEntity;
import com.akarbowy.spendingsapp.data.entities.GroupedCategories;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;


/**
 * Created by arek.karbowy on 20/10/2017.
 */

public class TransactionViewModel extends ViewModel {

    public static final int UNDEFINED_TRANSACTION_ID = 0;
    private TransactionRepository repository;
    public final LiveData<List<GroupedCategories>> groupedCategories;
    public final LiveData<List<CurrencyEntity>> currencies;
    public final LiveData<Transaction> transaction;

    private MutableLiveData<Integer> transactionId = new MutableLiveData<>();

    private Double value = null;
    private String name = null;
    private MutableLiveData<LocalDateTime> localDateTime = new MutableLiveData<>();
    private MutableLiveData<CurrencyEntity> currency = new MutableLiveData<>();
    private MutableLiveData<CategoryEntity> category = new MutableLiveData<>();

    public TransactionViewModel(TransactionRepository repository) {
        this.repository = repository;

        groupedCategories = repository.getCategories();
        currencies = repository.getCurrencies();
        transaction = Transformations.switchMap(transactionId, id -> {
            if (id == UNDEFINED_TRANSACTION_ID) {
                setLocalDateTime(LocalDateTime.now(ZoneOffset.UTC));
                setCurrency(App.DEFAULT_CURRENCY);
                setName("Transaction");
                return null;
            }

            return repository.loadTransaction(id);
        });
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public final LiveData<String> getFormattedDate() {
        return Transformations.map(localDateTime, input -> input.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime.getValue();
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime.setValue(localDateTime);
    }

    public MutableLiveData<CurrencyEntity> getChosenCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyEntity currency) {
        this.currency.setValue(currency);
    }

    public MutableLiveData<CategoryEntity> getChosenCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category.setValue(category);
    }

    public void start(int transactionId) {
        this.transactionId.setValue(transactionId);
    }

    public void onSaveTransaction() {
        TransactionEntity transaction = new TransactionEntity();
        transaction.transactionId = transactionId.getValue();
        transaction.categoryId = category.getValue().categoryEntityId;
        transaction.currencyId = currency.getValue().isoCode;
        transaction.title = name;
        transaction.value = value;
        transaction.date = localDateTime.getValue(); //TODO zones
        repository.saveTransaction(transaction);
    }

    public void onDeleteTransaction() {
        repository.updateTransaction(transaction.getValue().transaction);
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final TransactionRepository repository;

        public Factory(TransactionRepository repository) {
            this.repository = repository;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(TransactionViewModel.class)) {
                return (T) new TransactionViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

}
