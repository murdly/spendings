package com.akarbowy.spendingsapp.data.entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "currencies")
public class CurrencyEntity {

    @PrimaryKey
    @NonNull
    public String isoCode;
    public String name;
    public String symbol;

    public CurrencyEntity(String symbol, @NonNull String isoCode, String name) {
        this.symbol = symbol;
        this.isoCode = isoCode;
        this.name = name;
    }

    //TODO remove. for development.
    @Override public String toString() {
        return String.format("%1$s-%2$s-%3$s", symbol, isoCode, name);
    }
}