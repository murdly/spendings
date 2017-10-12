package com.akarbowy.spendingsapp;


import com.akarbowy.spendingsapp.data.entities.TransactionEntity;

import java.util.Date;
import java.util.Random;

class DbTestUtil {

    public static TransactionEntity createTransaction(int value, int currencyId, Date date) {
        return createTransaction("", date, value,1, currencyId);
    }

    public static TransactionEntity createTransaction(String title, Date date, int categoryId, int currencyId) {
        return createTransaction(title, date, new Random().nextInt(300), categoryId, currencyId);
    }

    public static TransactionEntity createTransaction(String title, Date date, int value, int categoryId, int currencyId) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.title = title;
        transactionEntity.date = date;
        transactionEntity.value = value;
        transactionEntity.categoryId = categoryId;
        transactionEntity.currencyId = currencyId;
        return transactionEntity;
    }


}
