package com.akarbowy.spendingsapp.ui.transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.PopulateUtil;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;

import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransactionActivity extends AppCompatActivity {

    private static final String EXTRA_TRANSACTION_ID = "extra_transaction_id";

    @BindView(R.id.transaction_currency_value) TextInputEditText currencyValue;

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

        currencyValue.setText("$");

    }

    @OnClick(R.id.transaction_save)
    public void onSaveClick() {
        add();
    }

    @OnClick(R.id.transaction_change_currency)
    public void onCurrencyChangeClick() {
        BottomSheetDialogFragment currencySheet = new CurrencyBottomSheet();
        currencySheet.show(getSupportFragmentManager(), "tag");

    }

    public void add() {
        TransactionEntity transactionEntity =
                PopulateUtil.createTransaction(new Random().nextInt(20) + "title", new Date(2017, 10, 15), 1, 1);

        AppDatabase.getInstance(this).transactionDao().insertTransaction(transactionEntity);

        Toast.makeText(this, "inserted: " + transactionEntity.toString(), Toast.LENGTH_SHORT).show();

        finish();
    }
}
