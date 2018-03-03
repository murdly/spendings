package com.akarbowy.spendingsapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.helpers.NoItemsHelper;
import com.akarbowy.spendingsapp.ui.OverviewViewModel;
import com.akarbowy.spendingsapp.ui.PeriodSpendingsAdapter;
import com.akarbowy.spendingsapp.ui.RecentTransactionsAdapter;
import com.akarbowy.spendingsapp.ui.home.PeriodDatePicker;
import com.akarbowy.spendingsapp.ui.importdata.ImportDataActivity;
import com.akarbowy.spendingsapp.ui.transaction.TransactionActivity;
import com.akarbowy.spendingsapp.ui.transaction.TransactionRepository;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.home_add)
    Button addNewView;
    @BindView(R.id.period_title)
    TextView periodTitleView;

    @BindView(R.id.period_from)
    TextView periodFromView;
    @BindView(R.id.gap)
    TextView periodGapView;
    @BindView(R.id.period_to)
    TextView periodToView;
    @BindView(R.id.group)
    Group customPeriodGroup;
    @BindView(R.id.spendings_list)
    RecyclerView spendingsList;
    @BindView(R.id.spendings_list_empty_view)
    View emptyViewForSpendingsList;
    @BindView(R.id.recent_list)
    RecyclerView recentTransactionsList;
    @BindView(R.id.recent_list_empty_view)
    View emptyViewForRecentList;

    private OverviewViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        vm = ViewModelProviders.of(this,
                new OverviewViewModel.Factory(new TransactionRepository(AppDatabase.getInstance(this))))
                .get(OverviewViewModel.class);

        setupOverview();

        setupRecentTransactions();
    }

    private void setupOverview() {
        final PeriodSpendingsAdapter spendingsAdapter = new PeriodSpendingsAdapter();
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.HORIZONTAL);
        spendingsList.setLayoutManager(layout);
        spendingsList.setAdapter(spendingsAdapter);
        new NoItemsHelper(emptyViewForSpendingsList).attachToRecyclerView(spendingsList);

        vm.periodicByCurrency.observe(this, periodSpendings -> {
            updatePeriodViews();
            spendingsAdapter.setItems(periodSpendings);
        });
    }

    private void updatePeriodViews() {
        periodTitleView.setText(String.format("%s %s", vm.getSelectedPeriodTitle(), getString(R.string.home_period_arrow)));

        periodFromView.setText(vm.getPeriodFromTitle());
        periodFromView.setEnabled(vm.isCustomPeriodSet());
        periodGapView.setEnabled(vm.isCustomPeriodSet());
        periodToView.setText(vm.getPeriodToTitle());
        periodToView.setEnabled(vm.isCustomPeriodSet());
    }

    private void setupRecentTransactions() {
        final RecentTransactionsAdapter adapter = new RecentTransactionsAdapter(this);
        adapter.setOnItemClickListener(item -> showUpdateDialog(new OnTransactionActionListener() {
            @Override
            public void onEdit() {
                startActivity(TransactionActivity.newEditIntent(MainActivity.this,
                        item.transaction.transactionId));
            }

            @Override
            public void onDelete() {
                vm.onDeleteTransaction(item.transaction);
            }
        }));
        recentTransactionsList.setLayoutManager(new LinearLayoutManager(this));
        recentTransactionsList.setAdapter(adapter);
        new NoItemsHelper(emptyViewForRecentList).attachToRecyclerView(recentTransactionsList);

        vm.transactions.observe(this, adapter::setList);
    }

    /*
    If custom period chosen,
    - make period boxes editable, with default values of current month, both from and to
    - set these values on period title view.
     */
    @OnClick(R.id.period_title)
    public void onPeriodsClick() {
        final String[] periodsTexts = {"This month", "Previous month", "All time", "Custom"};
        new AlertDialog.Builder(this)
                .setTitle("Period")
                .setItems(periodsTexts, (dialog, which) -> {
                    vm.setPeriod(which);
                    if (which == 3) {
                        customPeriodGroup.setVisibility(View.VISIBLE);
                    }
                })
                .show();
    }

    @OnClick(R.id.period_from)
    public void onPeriodFromClick() {
        new PeriodDatePicker().show(getSupportFragmentManager(), "periodFrom");
    }

    @OnClick(R.id.period_to)
    public void onPeriodToClick() {
        new PeriodDatePicker().show(getSupportFragmentManager(), "periodTo");
    }

    private void showUpdateDialog(final OnTransactionActionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update transaction");
        builder.setItems(new String[]{"Edit", "Delete"}, (dialog, which) -> {
            if (which == 0) {
                listener.onEdit();
            } else if (which == 1) {
                listener.onDelete();
            }
        });
        builder.show();
    }


    @OnClick(R.id.home_add)
    public void add() {
        startActivity(TransactionActivity.newAddIntent(this));
    }

    @OnClick(R.id.home_import)
    public void presentImportScreen() {
        startActivity(new Intent(this, ImportDataActivity.class));
    }

    public interface OnTransactionActionListener {
        void onEdit();

        void onDelete();
    }

}
