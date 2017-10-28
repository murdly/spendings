package com.akarbowy.spendingsapp.ui.transaction;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.AppDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransactionActivity extends AppCompatActivity {

    private static final String EXTRA_TRANSACTION_ID = "extra_transaction_id";

    @BindView(R.id.transaction_fields_date_value)
    TextInputEditText dateValue;
    @BindView(R.id.transaction_fields_currency_value)
    TextInputEditText currencyValue;
    @BindView(R.id.transaction_fields_category_value)
    TextInputEditText categoryValue;
    @BindView(R.id.transaction_container)
    ViewGroup containerView;

    TransactionViewModel viewModel;

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

        viewModel.start(transactionId);

        viewModel.transaction.observe(this, t -> {
            if(t != null){
                Log.d("xxTA2", t.transaction.toString() + "\n" +
                        t.category.toString() + "\n" +
                        t.currency.toString());
                viewModel.setLocalDate(t.transaction.date);
                viewModel.setCurrency(t.currency);
                viewModel.setCategory(t.category);
            }
        });

        viewModel.getFormattedDate().observe(this, s -> dateValue.setText(s));

        viewModel.getChosenCurrency().observe(this, currency -> currencyValue.setText(currency.isoCode));

        viewModel.getChosenCategory().observe(this, categoryEntity -> categoryValue.setText(categoryEntity.categoryName));
    }

    @OnClick(R.id.transaction_save)
    public void onSaveClick() {
        viewModel.saveTransaction();
        finish();
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
