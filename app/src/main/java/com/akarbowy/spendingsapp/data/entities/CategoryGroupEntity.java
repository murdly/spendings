package com.akarbowy.spendingsapp.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "category_groups")
public class CategoryGroupEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public CategoryGroupEntity(String name) {
        this.name = name;
    }

    //TODO remove. for development.
    @Override public String toString() {
        return String.format("%1$s-%2$s", id, name);
    }
}
