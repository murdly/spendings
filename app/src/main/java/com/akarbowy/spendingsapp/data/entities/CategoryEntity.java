package com.akarbowy.spendingsapp.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "categories")
public class CategoryEntity {

    @PrimaryKey(autoGenerate = true)
    public int categoryEntityId;

    @ColumnInfo(name = "group_id")
    public int groupId;

    public String categoryName;

    @Ignore
    public CategoryEntity(int groupId, String categoryName) {
        this.groupId = groupId;
        this.categoryName = categoryName;
    }

    public CategoryEntity(String categoryName) {
        this.categoryName = categoryName;
    }

    //TODO remove. for development.
    @Override public String toString() {
        return String.format("%1$s-%2$s-%3$s-%4$s", groupId != 0 ? "    " : "", categoryEntityId, groupId, categoryName);
    }
}
