package com.akarbowy.spendingsapp.data.entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "currencies")
public class CurrencyEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String symbol;

    //TODO remove. for development.
    @Override public String toString() {
        return String.format("%1$s-%2$s-%3$s", id, name, symbol);
    }
}