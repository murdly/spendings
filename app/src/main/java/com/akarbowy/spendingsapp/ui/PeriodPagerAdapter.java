package com.akarbowy.spendingsapp.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PeriodPagerAdapter extends FragmentStatePagerAdapter {

    public PeriodPagerAdapter(FragmentManager fm) {
        super(fm);
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

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new PeriodSpendingsFragment();
        Bundle args = new Bundle();
        args.putInt(PeriodSpendingsFragment.ARG_OBJECT, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 2) {
            BackgroundColorSpan bgspan = new BackgroundColorSpan(Color.BLUE);
            SpannableStringBuilder test = new SpannableStringBuilder("test");
            test.setSpan(bgspan, 0, 4, 0);
            return test;
        }
        return "OBJECT " + (position + 1);
    }

}
