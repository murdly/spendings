package com.akarbowy.spendingsapp.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import org.threeten.bp.LocalDate;


@Entity(tableName = "transactions",
        foreignKeys = {
                @ForeignKey(entity = CategoryEntity.class,
                        parentColumns = "categoryEntityId",
                        childColumns = "category_id"),
                @ForeignKey(entity = CurrencyEntity.class,
                        parentColumns = "isoCode",
                        childColumns = "currency_id")})
public class TransactionEntity {

    @PrimaryKey(autoGenerate = true)
    public int transactionId;

    public String title;
    public LocalDate date;
    public double value;
    public boolean deleted;

    @ColumnInfo(name = "category_id")
    public int categoryId;
    @ColumnInfo(name = "currency_id")
    public String currencyId;

    //TODO remove. for development.
    @Override public String toString() {
        return String.format("%1$s-%2$s-%3$s-%4$s-%5$s",
                transactionId, title, (int)value, date.toEpochDay(), deleted);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionEntity entity = (TransactionEntity) o;

        if (transactionId != entity.transactionId) return false;
        return deleted == entity.deleted;
    }

    @Override
    public int hashCode() {
        int result = transactionId;
        result = 31 * result + (deleted ? 1 : 0);
        return result;
    }
}
