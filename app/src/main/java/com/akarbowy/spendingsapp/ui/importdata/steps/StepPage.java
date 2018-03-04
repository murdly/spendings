package com.akarbowy.spendingsapp.ui.importdata.steps;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

public interface StepPage {

    @NonNull Fragment getFragment();

    String getPageTitle();

    int getPageOrdinal();

}
