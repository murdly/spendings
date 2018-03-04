package com.akarbowy.spendingsapp.ui.importdata.steps.currency;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.ui.currency.CurrencyViewModel;
import com.akarbowy.spendingsapp.ui.importdata.ImportViewModel;
import com.akarbowy.spendingsapp.ui.importdata.steps.StepPage;
import com.akarbowy.spendingsapp.ui.transaction.CurrencyAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrencyStepFragment extends Fragment implements StepPage {

    @BindView(R.id.list)
    RecyclerView listView;

    private ImportViewModel viewModel;

    private CurrencyAdapter adapter;

    private CurrencyViewModel currenciesViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.import_step_currency_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {

        initViewModels();

        initList();

        bindCurrencies();
    }

    private void initViewModels() {
        viewModel = ViewModelProviders.of(getActivity()).get(ImportViewModel.class);

        currenciesViewModel = ViewModelProviders.of(getActivity(),
                new CurrencyViewModel.Factory(AppDatabase.getInstance(getActivity())))
                .get(CurrencyViewModel.class);
    }

    private void initList() {
        adapter = new CurrencyAdapter();
        listView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(adapter);
    }

    private void bindCurrencies() {
        currenciesViewModel.currencies.observe(getActivity(), adapter::setItems);

        adapter.setCallback(currency -> viewModel.setCurrencyForData(currency));
    }

    @NonNull @Override public Fragment getFragment() {
        return this;
    }

    @Override public String getPageTitle() {
        return getString(R.string.import_step_currency_title);
    }

    @Override public int getPageOrdinal() {
        return 2;
    }

}
