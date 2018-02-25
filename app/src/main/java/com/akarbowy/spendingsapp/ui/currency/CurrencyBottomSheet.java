package com.akarbowy.spendingsapp.ui.currency;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;
import com.akarbowy.spendingsapp.ui.transaction.CurrencyAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrencyBottomSheet extends BottomSheetDialogFragment {

    @BindView(R.id.list)
    RecyclerView listView;

    private CurrencyAdapter adapter;

    private Callback callback;

    private CurrencyViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.transaction_currency_content, container);
        ButterKnife.bind(this, view);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {

        initViewModel();

        initAdapter();

        bindCurrencies();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(getActivity(),
                new CurrencyViewModel.Factory(AppDatabase.getInstance(getActivity())))
                .get(CurrencyViewModel.class);
    }

    private void initAdapter() {
        adapter = new CurrencyAdapter();
        listView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        listView.setAdapter(adapter);
    }

    private void bindCurrencies() {
        viewModel.currencies.observe(getActivity(), adapter::setItems);

        adapter.setCallback(this::onCurrencyChosen);
    }

    private void onCurrencyChosen(CurrencyEntity currency) {
        if (callback != null) {
            callback.onCurrencyChosen(getArguments(), currency);
        }
        dismiss();
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CurrencyBottomSheet.Callback) {
            callback = (CurrencyBottomSheet.Callback) context;
        }
    }


    public interface Callback {
        void onCurrencyChosen(Bundle bundle, CurrencyEntity chosen);
    }
}
