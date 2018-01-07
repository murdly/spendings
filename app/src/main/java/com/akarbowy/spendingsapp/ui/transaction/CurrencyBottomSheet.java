package com.akarbowy.spendingsapp.ui.transaction;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.akarbowy.spendingsapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrencyBottomSheet extends BottomSheetDialogFragment {

    @BindView(R.id.list)
    RecyclerView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_currency_content, container);
        ButterKnife.bind(this, view);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final CurrencyAdapter adapter = new CurrencyAdapter();
        listView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        listView.setAdapter(adapter);

        final TransactionViewModel viewModel = ViewModelProviders.of(getActivity()).get(TransactionViewModel.class);
        viewModel.currencies.observe(getActivity(), adapter::setItems);

        adapter.setCallback(chosen -> {
            viewModel.setCurrency(chosen);
            dismiss();
        });
    }
}
