package com.akarbowy.spendingsapp.ui.transaction;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.AppDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransactionActivity extends AppCompatActivity {

    private static final String EXTRA_TRANSACTION_ID = "extra_transaction_id";

    @BindView(R.id.transaction_fields_value_value)
    TextInputEditText valueValueInput;
    @BindView(R.id.transaction_fields_date_value)
    TextInputEditText dateValueInput;
    @BindView(R.id.transaction_fields_currency_value)
    TextInputEditText currencyValueInput;
    @BindView(R.id.transaction_fields_category_value)
    TextInputEditText categoryValueInput;
    @BindView(R.id.transaction_fields_name_value)
    TextInputEditText nameValueInput;
    @BindView(R.id.transaction_delete)
    Button deleteButton;
    @BindView(R.id.transaction_container)
    ViewGroup containerView;

    TransactionViewModel viewModel;
    TransactionDataValidator validator;

    public static Intent newAddIntent(Context context) {
        return new Intent(context, TransactionActivity.class);
    }

    public static Intent newEditIntent(Context context, int transactionId) {
        Intent intent = new Intent(context, TransactionActivity.class);
        intent.putExtra(EXTRA_TRANSACTION_ID, transactionId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this,
                new TransactionViewModel.Factory(new TransactionRepository(AppDatabase.getInstance(this))))
                .get(TransactionViewModel.class);

        int transactionId = getIntent().getIntExtra(EXTRA_TRANSACTION_ID, TransactionViewModel.UNDEFINED_TRANSACTION_ID);

        deleteButton.setVisibility(transactionId == TransactionViewModel.UNDEFINED_TRANSACTION_ID ? View.GONE : View.VISIBLE);

        viewModel.start(transactionId);

        viewModel.transaction.observe(this, t -> {
            if (t != null) {
                Log.d("xxTA2", t.toString());

                viewModel.setLocalDateTime(t.transaction.date);
                viewModel.setCurrency(t.currency);
                viewModel.setCategory(t.category);
                viewModel.setValue(t.transaction.value);
                viewModel.setName(t.transaction.title);

                valueValueInput.setText(String.valueOf(t.transaction.value));
                nameValueInput.setText(t.transaction.title);
            }
        });

        viewModel.getFormattedDate().observe(this, s -> dateValueInput.setText(s));

        viewModel.getChosenCurrency().observe(this, currency -> currencyValueInput.setText(currency.isoCode));

        viewModel.getChosenCategory().observe(this, categoryEntity -> categoryValueInput.setText(categoryEntity.categoryName));


        validator = new TransactionDataValidator(new TextInputEditText[]{
                valueValueInput, categoryValueInput, nameValueInput}, "Must be set");

        valueValueInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                Double value = !text.isEmpty() ? Double.valueOf(text) : null;
                viewModel.setValue(value);
            }
        });

        nameValueInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setName(s.toString());
            }
        });

    }

    @OnClick(R.id.transaction_delete)
    public void onDeleteClick() {
        viewModel.onDeleteTransaction();
        finish();
    }

    @OnClick(R.id.transaction_save)
    public void onSaveClick() {
        if (validator.validate()) {
            viewModel.onSaveTransaction();
            finish();
        }
    }

    @OnClick(R.id.transaction_fields_category_value)
    public void onCategoryValueClick() {
        CategoryBottomSheet sheet = new CategoryBottomSheet();
        sheet.show(getSupportFragmentManager(), "category");
    }

    @OnClick(R.id.transaction_fields_currency_value)
    public void onCurrencyValueClick() {
        CurrencyBottomSheet sheet = new CurrencyBottomSheet();
        sheet.show(getSupportFragmentManager(), "currency");
    }

    @OnClick(R.id.transaction_fields_date_value)
    public void onDateValueClick() {
        TransactionDatePicker picker = new TransactionDatePicker();
        picker.show(getSupportFragmentManager(), "date");
    }
}
