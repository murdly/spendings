package com.akarbowy.spendingsapp.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "transactions",
        foreignKeys = {
                @ForeignKey(entity = CategoryEntity.class,
                        parentColumns = "id",
                        childColumns = "category_id"),
                @ForeignKey(entity = CurrencyEntity.class,
                        parentColumns = "id",
                        childColumns = "currency_id")})
public class TransactionEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public Date date;
    public double value;
    public boolean deleted;

    @ColumnInfo(name = "category_id")
    public int categoryId;
    @ColumnInfo(name = "currency_id")
    public int currencyId;

    //TODO remove. for development.
    @Override public String toString() {
        return String.format("%1$s-%2$s-%3$s-%4$s-%5$s",
                id, title, (int)value, date.getTime(), deleted);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionEntity that = (TransactionEntity) o;

        return id == that.id && title.equals(that.title);
    }

    @Override public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        return result;
    }
}
