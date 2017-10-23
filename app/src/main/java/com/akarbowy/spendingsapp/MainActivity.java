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
import android.widget.TextView;
import android.widget.Toast;

import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.entities.PeriodSpendings;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;
import com.akarbowy.spendingsapp.ui.OverviewViewModel;
import com.akarbowy.spendingsapp.ui.PeriodSpendingsAdapter;
import com.akarbowy.spendingsapp.ui.RecentTransactionsAdapter;
import com.akarbowy.spendingsapp.ui.home.PeriodDatePicker;
import com.akarbowy.spendingsapp.ui.transaction.TransactionActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.home_add)
    Button addNewView;
    @BindView(R.id.list)
    RecyclerView recentTransactionsList;
    @BindView(R.id.spendings_list)
    RecyclerView spendingsList;

    @BindView(R.id.period_title)
    TextView periodTitleView;
    @BindView(R.id.period_from)
    TextView periodFromView;
    @BindView(R.id.period_to)
    TextView periodToView;
    @BindView(R.id.group)
    Group customPeriodGroup;

    private AppDatabase appDatabase;
    private PeriodSpendingsAdapter spendingsAdapter;
    private OverviewViewModel vm;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        appDatabase = AppDatabase.getInstance(this);

        vm = ViewModelProviders.of(this,
                new OverviewViewModel.Factory(AppDatabase.getInstance(this)))
                .get(OverviewViewModel.class);

        spendingsAdapter = new PeriodSpendingsAdapter();
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.HORIZONTAL);
        spendingsList.setLayoutManager(layout);
        spendingsList.setAdapter(spendingsAdapter);

        vm.periodicByCurrency.observe(this, new Observer<List<PeriodSpendings>>() {
            @Override
            public void onChanged(@Nullable List<PeriodSpendings> periodSpendings) {
                periodTitleView.setText(vm.getSelectedPeriodTitle());
                periodFromView.setText(vm.getPeriodFromTitle());
                periodToView.setText(vm.getPeriodToTitle());
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

    /*
    If custom period chosen,
    - make period boxes editable, with default values of current month, both from and to
    - set these values on period title view.
     */
    @OnClick({R.id.period_title, R.id.period_icon})
    public void onPeriodsClick() {
        final String[] periodsTexts = {"This month", "Previous month", "All time", "Custom"};
        new AlertDialog.Builder(this)
                .setTitle("Period")
                .setItems(periodsTexts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vm.setPeriod(which);
                        if (which == 3) {
                            customPeriodGroup.setVisibility(View.VISIBLE);
                        }
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
