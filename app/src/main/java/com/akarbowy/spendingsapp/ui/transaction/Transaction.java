package com.akarbowy.spendingsapp.ui.transaction;

import android.arch.persistence.room.Embedded;

import com.akarbowy.spendingsapp.data.entities.CategoryEntity;
import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;

/**
 * Created by arek.karbowy on 27/10/2017.
 */

public class Transaction {
    @Embedded
    public TransactionEntity transaction;
    @Embedded
    public CategoryEntity category;
    @Embedded
    public CurrencyEntity currency;
}
