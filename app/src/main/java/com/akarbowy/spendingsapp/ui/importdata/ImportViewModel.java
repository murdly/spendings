package com.akarbowy.spendingsapp.ui.importdata;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.akarbowy.spendingsapp.App;
import com.akarbowy.spendingsapp.data.entities.CategoryEntity;
import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;
import com.akarbowy.spendingsapp.data.external.ImportData;
import com.akarbowy.spendingsapp.data.external.ImportSource;
import com.akarbowy.spendingsapp.data.external.RevolutSource;
import com.akarbowy.spendingsapp.managers.ImportManager;
import com.akarbowy.spendingsapp.ui.transaction.TransactionRepository;

import java.io.InputStream;
import java.util.List;

import timber.log.Timber;

public class ImportViewModel extends ViewModel {

    private final TransactionRepository repository;

    MutableLiveData<List<ImportData>> importedData = new MutableLiveData<>();

    MutableLiveData<CurrencyEntity> dataCurrency = new MutableLiveData<>();

    ImportViewModel(TransactionRepository repository) {
        this.repository = repository;

        setCurrencyForData(App.DEFAULT_CURRENCY);
    }

    void importData(InputStream inputStream) {

        ImportManager.startImportData(inputStream, new ImportManager.Callback() {
            @Override public void onDataLoaded(List<String> data) {
                onDataImported(data);
            }

            @Override public void onDataError() {
                onImportError();
            }
        });
    }

    private void onImportError() {
        Timber.d("onImportError");
    }

    private void onDataImported(List<String> data) {
        if (data != null && !data.isEmpty()) {

            final List<? extends ImportSource> revolutData = RevolutSource.create(dataCurrency.getValue().isoCode, data, true);

            final List<ImportData> metaData = ImportData.Mapper.fromSource(revolutData);

            this.importedData.setValue(metaData);
        }
    }

    boolean saveData() {

        final List<ImportData> data = importedData.getValue();
        if (data != null) {

            final ImportDataValidator validator = new ImportDataValidator(data);

            if (!validator.validate()) {
                return false;
            } else {

                final List<TransactionEntity> transactions = ImportData.Mapper.toTransactions(data);

                repository.saveTransactions(transactions);
                return true;
            }
        }

        return false;
    }

    void setCategoryForItem(int itemPosition, CategoryEntity category) {

        final List<ImportData> data = importedData.getValue();

        final ImportData transaction = data.get(itemPosition);
        transaction.setChosenCategory(category.categoryEntityId, category.categoryName);

        data.set(itemPosition, transaction);

        this.importedData.setValue(data);
    }

    void setCurrencyForData(CurrencyEntity currency) {
        this.dataCurrency.setValue(currency);
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final TransactionRepository repository;

        public Factory(TransactionRepository repository) {
            this.repository = repository;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ImportViewModel.class)) {
                return (T) new ImportViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
