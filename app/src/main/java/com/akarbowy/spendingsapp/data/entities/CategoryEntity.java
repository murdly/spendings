package com.akarbowy.spendingsapp.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "categories")
public class CategoryEntity {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "parent_id")
    public int parentId;

    public String name;

    //TODO remove. for development.
    @Override public String toString() {
        return String.format("%1$s-%2$s-%3$s-%4$s", parentId != 0 ? "    " : "", id, parentId, name);
    }
}
