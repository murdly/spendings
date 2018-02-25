package com.akarbowy.spendingsapp.data.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class GroupedCategories {
    @Embedded
    public CategoryGroupEntity group;

    @Relation(parentColumn = "categoryGroupEntityId", entityColumn = "group_id")
    public List<CategoryEntity> categories;
}
