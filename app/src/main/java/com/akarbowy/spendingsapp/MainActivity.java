package com.akarbowy.spendingsapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.entities.PeriodSpendings;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;
import com.akarbowy.spendingsapp.ui.OverviewViewModel;
import com.akarbowy.spendingsapp.ui.PeriodSpendingsAdapter;
import com.akarbowy.spendingsapp.ui.RecentTransactionsAdapter;
import com.akarbowy.spendingsapp.ui.transaction.TransactionActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.home_periods)
    View periodsView;
    @BindView(R.id.home_add)
    Button addNewView;
    @BindView(R.id.list)
    RecyclerView recentTransactionsList;
    @BindView(R.id.spendings_list)
    RecyclerView spendingsList;

    @BindView(R.id.group)
    Group customPeriodGroup;

    private AppDatabase appDatabase;
    private PeriodSpendingsAdapter spendingsAdapter;
    private OverviewViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        appDatabase = AppDatabase.getInstance(this);

        vm = ViewModelProviders.of(this,
                new OverviewViewModel.OVMFactory(AppDatabase.getInstance(this)))
                .get(OverviewViewModel.class);

        vm.setPeriod(0);

//        periodsSpinner.setAdapter(new PeriodsAdapter(this, OverviewViewModel.PERIODS));
//        periodsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                vm.setPeriod(position);
//            }
//
//            @Override public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });


        spendingsAdapter = new PeriodSpendingsAdapter();
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.HORIZONTAL);
        spendingsList.setLayoutManager(layout);
        spendingsList.setAdapter(spendingsAdapter);

        vm.periodicByCurrency.observe(this, new Observer<List<PeriodSpendings>>() {
            @Override
            public void onChanged(@Nullable List<PeriodSpendings> periodSpendings) {
                spendingsAdapter.setItems(periodSpendings);
            }
        });

        final RecentTransactionsAdapter adapter = new RecentTransactionsAdapter();
        adapter.setOnItemClickListener(onTransactionItemClickListener);
        recentTransactionsList.setLayoutManager(new LinearLayoutManager(this));
        recentTransactionsList.setAdapter(adapter);


        vm.transactions.observe(this, new Observer<PagedList<TransactionEntity>>() {
            @Override
            public void onChanged(@Nullable PagedList<TransactionEntity> items) {
                adapter.setList(items);
                Toast.makeText(MainActivity.this, "adding items: " + items.size(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @OnClick(R.id.home_periods)
    public void onPeriodsClick() {
        new AlertDialog.Builder(this)
                .setTitle("Period")
                .setItems(new String[]{"This month", "Previous month", "All time", "Custom"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which < 3) {
                            vm.setPeriod(which);
                        } else if (which == 3) {
                            customPeriodGroup.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .show();
    }

    private RecentTransactionsAdapter.OnItemClickListener onTransactionItemClickListener = new RecentTransactionsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(final TransactionEntity item) {
            showUpdateDialog(new OnTransactionActionListener() {
                @Override
                public void onEdit() {
                    startActivity(TransactionActivity.newEditIntent(MainActivity.this, item.id));
                }

                @Override
                public void onDelete() {
                    vm.onDeleteRecentTransaction(item);
                }
            });
        }
    };

    private void showUpdateDialog(final OnTransactionActionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update transaction");
        builder.setItems(new String[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    listener.onEdit();
                } else if (which == 1) {
                    listener.onDelete();
                }
            }
        });
        builder.show();
    }


    @OnClick(R.id.home_add)
    public void add() {
        startActivity(TransactionActivity.newAddIntent(this));
    }

    public interface OnTransactionActionListener {
        void onEdit();

        void onDelete();
    }


}
