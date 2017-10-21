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

public class PeriodsAdapter extends ArrayAdapter<OverviewViewModel.PeriodType> {

    private OverviewViewModel.PeriodType[] periodTypes;

    public PeriodsAdapter(@NonNull Context context, OverviewViewModel.PeriodType[] periodTypes) {
        super(context, R.layout.home_spinner_view);
        this.periodTypes = periodTypes;
    }

    @NonNull @Override public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.home_spinner_view, parent, false);
        } else {
            view = convertView;
        }

        OverviewViewModel.PeriodType periodType = getItem(position);
        ((TextView) view.findViewById(R.id.period_title)).setText(periodType.title);
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

        OverviewViewModel.PeriodType periodType = getItem(position);
        ((TextView) view.findViewById(R.id.period_title)).setText(periodType.title);
        return view;
    }

    @Override public int getCount() {
        return periodTypes.length;
    }

    @Nullable @Override public OverviewViewModel.PeriodType getItem(int position) {
        return periodTypes[position];
    }

}
