package com.akarbowy.spendingsapp.ui.transaction;


import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.CurrencyDictionary;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrencyBottomSheet extends BottomSheetDialogFragment {

    @BindView(R.id.list)
    RecyclerView listView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_actions_currency, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TransactionViewModel viewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        listView.setLayoutManager(layoutManager);
        CurrencyAdapter adapter = new CurrencyAdapter();
        adapter.setItems(Arrays.asList(viewModel.getAvailableCurrencies()));
        listView.setAdapter(adapter);
    }
}
