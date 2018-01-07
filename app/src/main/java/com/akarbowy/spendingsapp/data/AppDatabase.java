package com.akarbowy.spendingsapp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.akarbowy.spendingsapp.data.daos.CategoryDao;
import com.akarbowy.spendingsapp.data.daos.CurrencyDao;
import com.akarbowy.spendingsapp.data.daos.TransactionDao;
import com.akarbowy.spendingsapp.data.entities.CategoryEntity;
import com.akarbowy.spendingsapp.data.entities.CategoryGroupEntity;
import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;

@Database(version = 26,
        entities = {
                CategoryEntity.class,
                CategoryGroupEntity.class,
                CurrencyEntity.class,
                TransactionEntity.class})
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final Object sLock = new Object();
    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,
                        "database-spendings")
//                        .allowMainThreadQueries() // only for developing
//                        .fallbackToDestructiveMigration() // only for developing
                        .build();
            }

            return INSTANCE;
        }
    }

    public abstract TransactionDao transactionDao();

    public abstract CategoryDao categoryDao();

    public abstract CurrencyDao currencyDao();
}
