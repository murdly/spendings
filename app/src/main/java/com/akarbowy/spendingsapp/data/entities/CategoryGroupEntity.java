package com.akarbowy.spendingsapp.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "category_groups")
public class CategoryGroupEntity {

    @PrimaryKey(autoGenerate = true)
    public int categoryGroupEntityId;

    public String groupName;

    public CategoryGroupEntity(String groupName) {
        this.groupName = groupName;
    }

    //TODO remove. for development.
    @Override public String toString() {
        return String.format("%1$s-%2$s", categoryGroupEntityId, groupName);
    }
}
