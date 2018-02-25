package com.akarbowy.spendingsapp.ui.importdata;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.entities.CategoryEntity;
import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;
import com.akarbowy.spendingsapp.data.external.ImportData;
import com.akarbowy.spendingsapp.ui.category.CategoryBottomSheet;
import com.akarbowy.spendingsapp.ui.currency.CurrencyBottomSheet;
import com.akarbowy.spendingsapp.ui.transaction.TransactionRepository;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImportDataActivity extends AppCompatActivity implements CategoryBottomSheet.Callback, CurrencyBottomSheet.Callback {


    private static final String KEY_CATEGORY_FOR_ITEM = "KEY_CATEGORY_FOR_ITEM";

    private static final int FILE_CHOOSER_REQUEST_CODE = 23;


    @BindView(R.id.import_currency_value) TextInputEditText currencyValueInput;
    @BindView(R.id.save_button) Button saveButton;
    @BindView(R.id.import_list) RecyclerView listView;
    @BindView(R.id.progress) ProgressBar progressView;

    private ImportViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_data);
        ButterKnife.bind(this);

        init();
    }

    private void init() {

        initViewModel();

        initImportedDataList();

        observeDataCurrency();
    }

    private void initViewModel() {

        final TransactionRepository transactionRepository =
                new TransactionRepository(AppDatabase.getInstance(this));

        viewModel = ViewModelProviders.of(this,
                new ImportViewModel.Factory(transactionRepository)).get(ImportViewModel.class);
    }

    private void observeDataCurrency() {
        viewModel.dataCurrency.observe(this, currency -> currencyValueInput.setText(currency.isoCode));
    }

    private void initImportedDataList() {

        final ImportDataAdapter importDataAdapter = new ImportDataAdapter(this);

        importDataAdapter.setCallback(this::showCategories);

        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(importDataAdapter);

        viewModel.importedData.observe(this, data ->
                onImportedDataListUpdated(importDataAdapter, data));
    }

    private void onImportedDataListUpdated(ImportDataAdapter importDataAdapter, List<ImportData> data) {
        importDataAdapter.refreshData(data);

        progressView.setVisibility(View.GONE);

        saveButton.setEnabled(true);
    }

    @OnClick(R.id.import_button)
    public void onImportButtonClicked() {
        showFileChooser();
    }

    private void showFileChooser() {

        final Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");

        startActivityForResult(intent, FILE_CHOOSER_REQUEST_CODE);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                importData(data.getData());
            }
        }
    }

    private void importData(Uri uri) {

        progressView.setVisibility(View.VISIBLE);

        try {

            final InputStream input = getContentResolver().openInputStream(uri);

            viewModel.importData(input);

        } catch (FileNotFoundException e) {

            progressView.setVisibility(View.GONE);

            Toast.makeText(this, "Unable to open this file.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.save_button)
    public void onSaveButtonClicked() {
        boolean notValid = !viewModel.saveData();

        if (notValid) {
            Toast.makeText(this, "Set required fields.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Imported data successfully saved.", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    private void showCategories(int forItemAtPosition) {
        final Bundle bundle = new Bundle();
        bundle.putInt(KEY_CATEGORY_FOR_ITEM, forItemAtPosition);

        CategoryBottomSheet.newInstance(bundle)
                .show(getSupportFragmentManager(), "category");
    }

    @Override public void onCategoryChosen(Bundle bundle, CategoryEntity category) {
        viewModel.setCategoryForItem(bundle.getInt(KEY_CATEGORY_FOR_ITEM), category);
    }

    @OnClick(R.id.import_currency_value)
    public void onCurrencyValueClick() {
        final CurrencyBottomSheet sheet = new CurrencyBottomSheet();
        sheet.show(getSupportFragmentManager(), "currency");
    }

    @Override public void onCurrencyChosen(Bundle bundle, CurrencyEntity chosen) {
        viewModel.setCurrencyForData(chosen);

        currencyValueInput.setEnabled(false);
    }
}
