package com.akarbowy.spendingsapp.ui.transaction;

import android.arch.persistence.room.Embedded;

import com.akarbowy.spendingsapp.data.entities.CategoryEntity;
import com.akarbowy.spendingsapp.data.entities.CategoryGroupEntity;
import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;

public class Transaction {
    @Embedded
    public TransactionEntity transaction;
    @Embedded
    public CategoryEntity category;
    @Embedded
    public CategoryGroupEntity categoryGroup;
    @Embedded
    public CurrencyEntity currency;

    public String toString() {
        return transaction.toString() + "\n" +
                category.toString() + "\n" +
                currency.toString()  + "\n" +
                categoryGroup.toString();
    }

}
