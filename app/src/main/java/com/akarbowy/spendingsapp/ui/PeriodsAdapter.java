package com.akarbowy.spendingsapp.ui;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.akarbowy.spendingsapp.R;

public class PeriodsAdapter extends ArrayAdapter<OverviewViewModel.Periodd> {

    private OverviewViewModel.Periodd[] periodds;

    public PeriodsAdapter(@NonNull Context context, OverviewViewModel.Periodd[] periodds) {
        super(context, R.layout.home_spinner_view);
        this.periodds = periodds;
    }

    @NonNull @Override public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.home_spinner_view, parent, false);
        } else {
            view = convertView;
        }

        OverviewViewModel.Periodd periodd = getItem(position);
        ((TextView) view.findViewById(R.id.period_item_title)).setText(periodd.title);
        return view;
    }

    @Override public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.home_spinner_dropdown, parent, false);
        } else {
            view = convertView;
        }

        OverviewViewModel.Periodd periodd = getItem(position);
        ((TextView) view.findViewById(R.id.period_item_title)).setText(periodd.title);
        return view;
    }

    @Override public int getCount() {
        return periodds.length;
    }

    @Nullable @Override public OverviewViewModel.Periodd getItem(int position) {
        return periodds[position];
    }

}
