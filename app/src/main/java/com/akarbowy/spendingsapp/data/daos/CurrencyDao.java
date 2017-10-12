package com.akarbowy.spendingsapp.data.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;

import java.util.List;

@Dao
public interface CurrencyDao {

    @Insert void insert(CurrencyEntity currency);

    @Insert void insert(List<CurrencyEntity> currencies);

    @Query("SELECT * FROM currencies") List<CurrencyEntity> all();
}
