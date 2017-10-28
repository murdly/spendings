package com.akarbowy.spendingsapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.PopulateUtil;
import com.akarbowy.spendingsapp.data.daos.CategoryDao;
import com.akarbowy.spendingsapp.data.daos.CurrencyDao;
import com.akarbowy.spendingsapp.data.daos.TransactionDao;
import com.akarbowy.spendingsapp.data.entities.CategoryEntity;
import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;
import com.akarbowy.spendingsapp.data.entities.PeriodSpendings;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DbDaosTest {

    private static List<CategoryEntity> CATEGORIES = PopulateUtil.createListOfCategories();
    private static List<CurrencyEntity> CURRENCIES = PopulateUtil.createListOfCurrencies();
    private TransactionDao transactionDao;
    private CategoryDao categoryDao;
    private CurrencyDao currencyDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        transactionDao = db.transactionDao();
        categoryDao = db.categoryDao();
        currencyDao = db.currencyDao();

        categoryDao.insert(CATEGORIES);
        currencyDao.insert(CURRENCIES);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void has_predefinedCategories() throws InterruptedException {
        List<CategoryEntity> all = LiveDataTestUtil.getValue(categoryDao.all());
        assertEquals(CATEGORIES.size(), all.size());
    }

    @Test
    public void has_predefinedCurrencies() {
        assertEquals(CURRENCIES.size(), currencyDao.all().size());
    }

    @Test
    public void addSubCategory() {
        int lastId = CATEGORIES.get(CATEGORIES.size() - 1).categoryEntityId;
        int categoryId = 1;
        CategoryEntity sub = PopulateUtil.createCategory(lastId + 1, categoryId, "sub");
        categoryDao.insert(sub);
        assertEquals(5, categoryDao.getSubCategories(categoryId).size());
    }

    @Test
    public void addCategory() {
        int lastId = CATEGORIES.get(CATEGORIES.size() - 1).categoryEntityId;
        CategoryEntity sub = PopulateUtil.createCategory(lastId + 1, 0, "cat");
        categoryDao.insert(sub);
        assertEquals(4, categoryDao.getCategories().size());
    }

    @Test
    public void addTransaction() throws Exception {
        int categoryId = CATEGORIES.get(0).categoryEntityId;
        int currencyId = CURRENCIES.get(0).id;
        TransactionEntity t = DbTestUtil.createTransaction("drugi w gbp", new Date(2017, 1, 5), categoryId, currencyId);
        transactionDao.insertTransaction(t);

        List<TransactionEntity> all = LiveDataTestUtil.getValue(transactionDao.all());
        TransactionEntity lastTransaction = all.get(all.size() - 1);

        assertEquals(t.categoryId, lastTransaction.categoryId);
    }

    @Test(expected = SQLiteConstraintException.class)
    public void canAddCategory_duplicate_shouldThrowException() {
        CategoryEntity duplicate = PopulateUtil.createCategory(1, 0, "Transportation");
        categoryDao.insert(duplicate);
    }

    @Test(expected = SQLiteConstraintException.class)
    public void canAddCurrency_duplicate_shouldThrowException() {
        CurrencyEntity duplicate = PopulateUtil.createCurrency(2, "Euro", "EUR");
        currencyDao.insert(duplicate);
    }

    @Test
    public void valueOfTransactions_duringPeriod_ByCurrencies() throws InterruptedException{
        List<TransactionEntity> list = new ArrayList<>();

        Date from = new Date(2017, 12, 3);
        Date to = new Date(2017, 12, 25);
        Date date = new Date(2017, 12, 12);

        CurrencyEntity currency = CURRENCIES.get(0);
        float expectedSpendingsInCurrency = 45;
        list.add(DbTestUtil.createTransaction(25, currency.id, date));
        list.add(DbTestUtil.createTransaction(5, currency.id, date));
        list.add(DbTestUtil.createTransaction(15, currency.id, date));
        list.add(DbTestUtil.createTransaction(15, 2, date));
        list.add(DbTestUtil.createTransaction(15, 2, date));

        transactionDao.insert(list);

        List<PeriodSpendings> allByCurrency = LiveDataTestUtil.getValue(transactionDao.byCurrencyBetween(from, to));

        float totalSpendingsInCurrency = 0;
        for (PeriodSpendings spendings : allByCurrency) {
            if(spendings.name.equals(currency.name)){
                totalSpendingsInCurrency = (float) spendings.total;
                break;
            }
        }


        assertEquals(expectedSpendingsInCurrency, totalSpendingsInCurrency, 0.1);

    }

}
