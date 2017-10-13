package com.akarbowy.spendingsapp.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.entities.PeriodSpendings;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PeriodSpendingsFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    @BindView(R.id.period_spendings_list)
    RecyclerView listView;

    private PeriodSpendingsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_period_spendings, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new PeriodSpendingsAdapter();
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listView.setAdapter(adapter);

        OverviewViewModel vm = ViewModelProviders.of(this,
                new OverviewViewModel.OVMFactory(AppDatabase.getInstance(getActivity())))
                .get(OverviewViewModel.class);


        int pos = getArguments().getInt(ARG_OBJECT);
        vm.setPeriod(PeriodPagerAdapter.periods().get(pos));

        vm.periodicByCurrency.observe(this, new Observer<List<PeriodSpendings>>() {
            @Override public void onChanged(@Nullable List<PeriodSpendings> entities) {
                adapter.setItems(entities);

            }
        });

    }
}
