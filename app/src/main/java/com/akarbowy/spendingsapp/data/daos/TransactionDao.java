package com.akarbowy.spendingsapp.data.daos;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListProvider;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.akarbowy.spendingsapp.data.entities.PeriodSpendings;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;
import com.akarbowy.spendingsapp.ui.transaction.Transaction;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.List;

@Dao
public interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTransaction(TransactionEntity transactionEntity);

    @Insert
    long[] insert(List<TransactionEntity> entities);

    @Query("SELECT * FROM transactions ORDER BY date ASC")
    LiveData<List<TransactionEntity>> all();

    @Query("SELECT currencies.name, currencies.symbol, SUM (transactions.value) AS total FROM transactions " +
            "LEFT JOIN currencies ON transactions.currency_id = currencies.isoCode " +
            "WHERE transactions.date between :from and :to AND transactions.deleted = 0 " +
            "GROUP BY transactions.currency_id ORDER BY total")
    LiveData<List<PeriodSpendings>> byCurrencyBetween(LocalDateTime from, LocalDateTime to);

    @Query("SELECT currencies.name, currencies.symbol, SUM (transactions.value) AS total FROM transactions " +
            "LEFT JOIN currencies ON transactions.currency_id = currencies.isoCode " +
            "GROUP BY transactions.currency_id ORDER BY total")
    LiveData<List<PeriodSpendings>> byCurrency();

    @Query("SELECT * FROM transactions " +
            "INNER JOIN categories ON transactions.category_id = categories.categoryEntityId " +
            "INNER JOIN category_groups ON categories.group_id = category_groups.categoryGroupEntityId " +
            "INNER JOIN currencies ON transactions.currency_id = currencies.isoCode " +
            "ORDER BY date DESC")
    LivePagedListProvider<Integer, Transaction> allByNewestFirst();

    @Query("SELECT * FROM transactions " +
            "INNER JOIN categories ON transactions.category_id = categories.categoryEntityId " +
            "INNER JOIN category_groups ON categories.group_id = category_groups.categoryGroupEntityId " +
            "INNER JOIN currencies ON transactions.currency_id = currencies.isoCode " +
            "WHERE transactions.transactionId = :id")
    LiveData<Transaction> getById(Integer id);
}
