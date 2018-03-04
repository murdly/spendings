package com.akarbowy.spendingsapp.ui.importdata;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.net.Uri;

import com.akarbowy.spendingsapp.data.entities.CategoryEntity;
import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;
import com.akarbowy.spendingsapp.data.external.ImportData;
import com.akarbowy.spendingsapp.data.external.RevolutSource;
import com.akarbowy.spendingsapp.managers.ImportManager;
import com.akarbowy.spendingsapp.ui.importdata.steps.StepPage;
import com.akarbowy.spendingsapp.ui.importdata.steps.currency.CurrencyStepFragment;
import com.akarbowy.spendingsapp.ui.importdata.steps.list.ListStepFragment;
import com.akarbowy.spendingsapp.ui.importdata.steps.source.SourceStepFragment;
import com.akarbowy.spendingsapp.ui.transaction.TransactionRepository;

import java.io.InputStream;
import java.util.List;

import timber.log.Timber;

public class ImportViewModel extends ViewModel {

    public static final int STEPS_COUNT = 3;

    public final MutableLiveData<List<ImportData>> importedData = new MutableLiveData<>();

    public final MutableLiveData<StepPage> currentStepPage = new MutableLiveData<>();

    public final MutableLiveData<Boolean> isProgressVisible = new MutableLiveData<>();

    public final MutableLiveData<Boolean> isSaveVisible = new MutableLiveData<>();

    private final TransactionRepository repository;

    private Uri sourceFileUri;

    private CurrencyEntity currency;

    ImportViewModel(TransactionRepository repository) {
        this.repository = repository;

        setProgressVisibility(false);
        setSaveVisibility(false);
        setCurrentStepPage(new SourceStepFragment());
    }

    public void setCurrentStepPage(StepPage page) {
        this.currentStepPage.setValue(page);
    }

    public void setCurrencyForData(CurrencyEntity currency) {
        this.currency = currency;

        setCurrentStepPage(new ListStepFragment());
    }

    public void setProgressVisibility(boolean visible) {
        isProgressVisible.setValue(visible);
    }

    public void setSaveVisibility(boolean visible) {
        isSaveVisible.setValue(visible);
    }

    public void importData(InputStream inputStream) {

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
        setProgressVisibility(false);

        Timber.d("onImportError");
    }

    private void onDataImported(List<String> data) {
        setProgressVisibility(false);

        if (data != null && !data.isEmpty()) {

            final List<? extends ImportSource> revolutData = RevolutSource.create(currency.isoCode, data, true);

            final List<ImportData> metaData = ImportData.Mapper.fromSource(revolutData);

            this.importedData.setValue(metaData);
        }

    }

    void saveData() {

        final List<ImportData> data = importedData.getValue();
        if (data != null) {

            final List<TransactionEntity> transactions = ImportData.Mapper.toTransactions(data);

            repository.saveTransactions(transactions);
        }
    }

    public void setCategoryForItem(int itemPosition, CategoryEntity category) {

        final List<ImportData> data = importedData.getValue();

        final ImportData transaction = data.get(itemPosition);
        transaction.setChosenCategory(category.categoryEntityId, category.categoryName);

        data.set(itemPosition, transaction);

        this.importedData.setValue(data);
    }

    public boolean isValid() {
        return importedData.getValue() != null && new ImportDataValidator(importedData.getValue()).validate();
    }

    public Uri getSourceFileUri() {
        return sourceFileUri;
    }

    public void setSourceFileUri(Uri sourceFileUri) {
        this.sourceFileUri = sourceFileUri;

        setCurrentStepPage(new CurrencyStepFragment());
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
