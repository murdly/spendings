package com.akarbowy.spendingsapp.data.daos;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListProvider;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.akarbowy.spendingsapp.data.entities.PeriodSpendings;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface TransactionDao {

    @Insert long insertTransaction(TransactionEntity transactionEntity);

    @Insert long[] insert(List<TransactionEntity> entities);

    @Update int updateTransaction(TransactionEntity transactionEntity);

    //@Delete
    //Deleted entries will be shown as disabled, so they should be updated instead

    @Query("SELECT * FROM transactions ORDER BY date ASC") LiveData<List<TransactionEntity>> all();

    @Query("SELECT currencies.name, currencies.symbol, SUM (transactions.value) AS total FROM transactions " +
            "LEFT JOIN currencies ON transactions.currency_id = currencies.id " +
            "WHERE transactions.date between :from and :to AND transactions.deleted = 0 " +
            "GROUP BY transactions.currency_id ORDER BY total")
    LiveData<List<PeriodSpendings>> byCurrencyBetween(Date from, Date to);

    @Query("SELECT currencies.name, currencies.symbol, SUM (transactions.value) AS total FROM transactions " +
            "LEFT JOIN currencies ON transactions.currency_id = currencies.id " +
            "GROUP BY transactions.currency_id ORDER BY total")
    LiveData<List<PeriodSpendings>> byCurrency();


    @Query("SELECT * FROM transactions ORDER BY title ASC")
    LivePagedListProvider<Integer, TransactionEntity> allByTitle();
}
