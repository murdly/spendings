package com.akarbowy.spendingsapp.data.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by arek.karbowy on 26/10/2017.
 */

public class GroupedCategories {
    @Embedded
    public CategoryGroupEntity group;

    @Relation(parentColumn = "id", entityColumn = "group_id")
    public List<CategoryEntity> categories;
}
