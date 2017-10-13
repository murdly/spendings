package com.akarbowy.spendingsapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.PopulateUtil;
import com.akarbowy.spendingsapp.data.entities.PeriodSpendings;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;
import com.akarbowy.spendingsapp.ui.OverviewViewModel;
import com.akarbowy.spendingsapp.ui.PeriodSpendingsAdapter;
import com.akarbowy.spendingsapp.ui.PeriodsAdapter;
import com.akarbowy.spendingsapp.ui.RecentTransactionsAdapter;

import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.addNew) Button addNewView;
    @BindView(R.id.list) RecyclerView recentTransactionsList;
    @BindView(R.id.spendings_list) RecyclerView spendingsList;
    @BindView(R.id.period_spinner) Spinner periodsSpinner;

    private AppDatabase appDatabase;
    private PeriodSpendingsAdapter spendingsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        appDatabase = AppDatabase.getInstance(this);

        final OverviewViewModel vm = ViewModelProviders.of(this,
                new OverviewViewModel.OVMFactory(AppDatabase.getInstance(this)))
                .get(OverviewViewModel.class);

        periodsSpinner.setAdapter(new PeriodsAdapter(this, OverviewViewModel.PERIODS));
        periodsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    vm.setPeriod(position);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spendingsAdapter = new PeriodSpendingsAdapter();
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.HORIZONTAL);
        spendingsList.setLayoutManager(layout);
        spendingsList.setAdapter(spendingsAdapter);

        vm.periodicByCurrency.observe(this, new Observer<List<PeriodSpendings>>() {
            @Override public void onChanged(@Nullable List<PeriodSpendings> periodSpendings) {
                spendingsAdapter.setItems(periodSpendings);
            }
        });

        final RecentTransactionsAdapter adapter = new RecentTransactionsAdapter();
        recentTransactionsList.setLayoutManager(new LinearLayoutManager(this));
        recentTransactionsList.setAdapter(adapter);


        vm.transactions.observe(this, new Observer<PagedList<TransactionEntity>>() {
            @Override public void onChanged(@Nullable PagedList<TransactionEntity> items) {
                adapter.setList(items);
                Toast.makeText(MainActivity.this, "adding items: " + items.size(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    @OnClick(R.id.addNew) public void add() {
        TransactionEntity transactionEntity =
                PopulateUtil.createTransaction(new Random().nextInt(20) + "title", new Date(2017, 10, 15), 1, 1);

        appDatabase.transactionDao().insertTransaction(transactionEntity);
    }


}
