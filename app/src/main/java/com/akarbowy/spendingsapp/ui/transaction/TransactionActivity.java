package com.akarbowy.spendingsapp.ui.transaction;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

    private TransactionViewModel viewModel;
    private TransactionDataValidator validator;

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

        init();
    }

    private void init() {
        viewModel = ViewModelProviders.of(this,
                new TransactionViewModel.Factory(new TransactionRepository(AppDatabase.getInstance(this))))
                .get(TransactionViewModel.class);

        validator = new TransactionDataValidator(new TextInputEditText[]{
                valueValueInput, categoryValueInput, nameValueInput}, "Must be set");

        initTransaction();

        observeTransactionDataChanges();
    }

    private void initTransaction() {
        final int transactionId = getIntent().getIntExtra(EXTRA_TRANSACTION_ID, TransactionViewModel.UNDEFINED_TRANSACTION_ID);
        viewModel.start(transactionId);
        viewModel.transaction.observe(this, this::setTransactionData);

        final boolean isEditingTransaction = transactionId != TransactionViewModel.UNDEFINED_TRANSACTION_ID;
        deleteButton.setVisibility(isEditingTransaction ? View.VISIBLE : View.GONE);
    }

    private void setTransactionData(Transaction t) {
        if (t != null) {
            viewModel.setLocalDateTime(t.transaction.date);
            viewModel.setCurrency(t.currency);
            viewModel.setCategory(t.category);
            viewModel.setValue(t.transaction.value);
            viewModel.setName(t.transaction.title);

            valueValueInput.setText(String.valueOf(t.transaction.value));
            nameValueInput.setText(t.transaction.title);
        }
    }

    private void observeTransactionDataChanges() {
        viewModel.getFormattedDate().observe(this, s -> dateValueInput.setText(s));

        viewModel.getChosenCurrency().observe(this, currency -> currencyValueInput.setText(currency.isoCode));

        viewModel.getChosenCategory().observe(this, categoryEntity -> categoryValueInput.setText(categoryEntity.categoryName));

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

        valueValueInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String text = s.toString();
                final Double value = !text.isEmpty() ? Double.valueOf(text) : null;
                viewModel.setValue(value);
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
        final CategoryBottomSheet sheet = new CategoryBottomSheet();
        sheet.show(getSupportFragmentManager(), "category");
    }

    @OnClick(R.id.transaction_fields_currency_value)
    public void onCurrencyValueClick() {
        final CurrencyBottomSheet sheet = new CurrencyBottomSheet();
        sheet.show(getSupportFragmentManager(), "currency");
    }

    @OnClick(R.id.transaction_fields_date_value)
    public void onDateValueClick() {
        final TransactionDatePicker picker = new TransactionDatePicker();
        picker.show(getSupportFragmentManager(), "date");
    }
}
