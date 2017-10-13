package com.akarbowy.spendingsapp.data;


import com.akarbowy.spendingsapp.data.entities.CategoryEntity;
import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PopulateUtil {

    public static List<CategoryEntity> createListOfCategories() {
        List<CategoryEntity> list = new ArrayList<>();
        list.add(createCategory(1, 0, "Transportation"));
        list.add(createCategory(2, 1, "Tube"));
        list.add(createCategory(3, 1, "Taxi"));
        list.add(createCategory(4, 1, "Bus"));
        list.add(createCategory(5, 1, "Other"));

        list.add(createCategory(6, 0, "Gifts"));
        list.add(createCategory(7, 6, "Family"));
        list.add(createCategory(8, 6, "Friends"));
        list.add(createCategory(9, 6, "Other"));

        list.add(createCategory(10, 0, "Food"));
        list.add(createCategory(11, 10, "Grocery"));
        list.add(createCategory(12, 10, "Restaurant"));
        list.add(createCategory(13, 10, "Other"));

        return list;
    }

    public static CategoryEntity createCategory(int id, int parentId, String name) {
        CategoryEntity entity = new CategoryEntity();
        entity.id = id;
        entity.parentId = parentId;
        entity.name = name;
        return entity;
    }

    public static List<CurrencyEntity> createListOfCurrencies() {
        List<CurrencyEntity> list = new ArrayList<>();
        list.add(createCurrency(1, "Polish Zloty", "PLN"));
        list.add(createCurrency(2, "Euro", "EUR"));
        list.add(createCurrency(3, "British Pound", "GBP"));
        return list;
    }

    public static CurrencyEntity createCurrency(int id, String name, String symbol) {
        CurrencyEntity entity = new CurrencyEntity();
        entity.id = id;
        entity.name = name;
        entity.symbol = symbol;
        return entity;
    }

    public static List<TransactionEntity> createListOfTransactions() {
        List<TransactionEntity> list = new ArrayList<>();
        list.add(createTransaction("zakupy w pln, 2015", new Date(2015, 12, 3), 3, 1));
        list.add(createTransaction("zakupy w pln, 2015", new Date(2015, 12, 3), 3, 1));
        list.add(createTransaction("zakupy w pln", new Date(2017, 12, 3), 3, 1));
        list.add(createTransaction("bilet w euro", new Date(2017, 12, 22),7, 2));
        list.add(createTransaction("ksiazka w pln", new Date(2017, 12, 12),6, 1));
        list.add(createTransaction("raz w gbp", new Date(2017, 1, 11),11, 3));
        list.add(createTransaction("drugi w gbp", new Date(2017, 1, 5),13, 3));
        list.add(createTransaction("drug2i w gbp", new Date(2017, 5, 1),4, 3));
        list.add(createTransaction("dxxrusgi w gbp", new Date(2017, 6, 25),3, 2));
        list.add(createTransaction("10 w gbp", new Date(2017, 10, 12),13, 3));
        list.add(createTransaction("9 w eur", new Date(2017, 9, 12),13, 2));
        list.add(createTransaction("9 w eur", new Date(2017, 9, 12),13, 2));
        list.add(createTransaction("9 w eur", new Date(2017, 9, 12),13, 2));
        list.add(createTransaction("9 w eur", new Date(2017, 9, 22),13, 2));
        list.add(createTransaction("9 w eur", new Date(2017, 9, 22),13, 2));
        list.add(createTransaction("9 w eur", new Date(2017, 9, 23),13, 2));
        list.add(createTransaction("10 w gbp", new Date(2017, 10, 12),13, 3));
        list.add(createTransaction("10 w eur", new Date(2017, 10, 12),13, 2));
        list.add(createTransaction("10 w eur", new Date(2017, 10, 12),13, 2));
        list.add(createTransaction("10 w gbp", new Date(2017, 10, 12),13, 3));
        return list;
    }

    public static TransactionEntity createTransaction(String title, Date date, int categoryId, int currencyId) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.title = title;
        transactionEntity.date = date;
        transactionEntity.value = new Random().nextInt(300);
        transactionEntity.categoryId = categoryId;
        transactionEntity.currencyId = currencyId;
        return transactionEntity;
    }
}
