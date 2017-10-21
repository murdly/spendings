package com.akarbowy.spendingsapp.ui.transaction;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.CurrencyDictionary;
import com.akarbowy.spendingsapp.data.PopulateUtil;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransactionActivity extends AppCompatActivity {

    private static final String EXTRA_TRANSACTION_ID = "extra_transaction_id";

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
//        currencyValue.setText("$");
//        currencyValue.setEnabled(false);

        viewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);

        CurrencyDictionary[] availableCurrencies = viewModel.getAvailableCurrencies();


    }

    @OnClick(R.id.transaction_save)
    public void onSaveClick() {
        add();
    }

    @OnClick(R.id.transaction_fields_currency_value)
    public void onCurrencyValueClick() {
        CurrencyBottomSheet sheet = new CurrencyBottomSheet();
        sheet.show(getSupportFragmentManager(), "");
    }

    @OnClick(R.id.transaction_fields_date_value)
    public void onDateValueClick() {
        View view = LayoutInflater.from(this).inflate(R.layout.transaction_actions_date, containerView, false);
//        //TODO ignore if already displayed
//
//        DatePicker datePicker = view.findViewById(R.id.list);
//
//        containerView.removeAllViews();
//        containerView.addView(view);

        DateBottomSheet sheet = new DateBottomSheet();
        sheet.show(getSupportFragmentManager(), "");

    }


    public void add() {
        TransactionEntity transactionEntity =
                PopulateUtil.createTransaction(new Random().nextInt(20) + "title", new Date(2017, 10, 15), 1, 1);

        AppDatabase.getInstance(this).transactionDao().insertTransaction(transactionEntity);

        Toast.makeText(this, "inserted: " + transactionEntity.toString(), Toast.LENGTH_SHORT).show();

        finish();
    }
}
