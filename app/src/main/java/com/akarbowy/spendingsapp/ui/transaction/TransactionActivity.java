package com.akarbowy.spendingsapp.ui.transaction;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.Toast;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.CurrencyDictionary;
import com.akarbowy.spendingsapp.data.PopulateUtil;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;

import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransactionActivity extends AppCompatActivity {

    private static final String EXTRA_TRANSACTION_ID = "extra_transaction_id";

    @BindView(R.id.transaction_fields_date_value)
    TextInputEditText dateValue;
    @BindView(R.id.transaction_fields_currency_value)
    TextInputEditText currencyValue;
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
                new TransactionViewModel.Factory(AppDatabase.getInstance(this)))
                .get(TransactionViewModel.class);

        viewModel.getFormattedDate().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                dateValue.setText(s);
            }
        });

        viewModel.getChosenCurrency().observe(this, new Observer<CurrencyDictionary>() {
            @Override
            public void onChanged(@Nullable CurrencyDictionary currencyDictionary) {
                currencyValue.setText(currencyDictionary.isoCode);
            }
        });
    }

    @OnClick(R.id.transaction_save)
    public void onSaveClick() {
        add();
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

    public void add() {
        TransactionEntity transactionEntity =
                PopulateUtil.createTransaction(new Random().nextInt(20) + "title", new Date(2017, 10, 15), 1, 1);

        AppDatabase.getInstance(this).transactionDao().insertTransaction(transactionEntity);

        Toast.makeText(this, "inserted: " + transactionEntity.toString(), Toast.LENGTH_SHORT).show();

        finish();
    }
}
