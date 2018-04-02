package com.akarbowy.spendingsapp.data.entities;


import android.arch.persistence.room.Ignore;

public class PeriodSpendingsData {

    public double total;

    public String isoCode;
    public String symbol;
    public String name;

    /**
     * Estimated value in current base currency.
     */
    @Ignore
    public double estimation;
}
