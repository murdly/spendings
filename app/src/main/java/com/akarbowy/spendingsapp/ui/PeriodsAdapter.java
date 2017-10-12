package com.akarbowy.spendingsapp.ui;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PeriodsAdapter extends ArrayAdapter<String> {

    private static final int PERIOD_THIS_MONTH = 0;
    private static final int PERIOD_PREVIOUS_MONTH = 1;
    private static final int PERIOD_ALL_TIME = 2;
    private static final int PERIOD_CUSTOM = 3;

    public PeriodsAdapter(@NonNull Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    public static List<OverviewViewModel.Period> periods() {
        List<OverviewViewModel.Period> list = new ArrayList<>();

        list.add(new OverviewViewModel.Period(
                new Date(2017, 5, 1), new Date(2017, 5, 25)));
        list.add(new OverviewViewModel.Period(
                new Date(2017, 6, 1), new Date(2017, 6, 25)));
        list.add(new OverviewViewModel.Period(
                new Date(2017, 7, 1), new Date(2017, 7, 25)));
        list.add(new OverviewViewModel.Period(
                new Date(2017, 8, 1), new Date(2017, 8, 25)));
        list.add(new OverviewViewModel.Period(
                new Date(2017, 10, 1), new Date(2017, 10, 25)));

        return list;
    }

    @NonNull @Override public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        String txt = getItem(position);
        ((TextView) view.findViewById(android.R.id.text1)).setText(txt);
        return view;
    }

    @Override public int getCount() {
        return 4;
    }

    @Nullable @Override public String getItem(int position) {
        if (position == PERIOD_THIS_MONTH) return "This month";
        if (position == PERIOD_PREVIOUS_MONTH) return "Previous month";
        if (position == PERIOD_ALL_TIME) return "All time";

        return "Custom";
    }
}
