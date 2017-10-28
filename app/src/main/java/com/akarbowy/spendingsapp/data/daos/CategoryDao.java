package com.akarbowy.spendingsapp.data.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.akarbowy.spendingsapp.data.entities.CategoryEntity;
import com.akarbowy.spendingsapp.data.entities.CategoryGroupEntity;
import com.akarbowy.spendingsapp.data.entities.GroupedCategories;

import java.util.List;

@Dao
public abstract class CategoryDao {

    @Insert
    public abstract long insert(CategoryGroupEntity group);

    @Transaction
    public void insert(List<GroupedCategories> groups){
        for (GroupedCategories group : groups) {
            long id = insert(group.group);

            for (CategoryEntity category : group.categories) {
                category.groupId = (int) id;
                insert(category);
            }
        }
    }

    @Query("SELECT * FROM category_groups")
    public abstract LiveData<List<GroupedCategories>> allGrouped();

    @Insert
    public abstract void insert(CategoryEntity category);

//    @Insert
//    public abstract  void insert(List<CategoryEntity> categories);

    @Query("SELECT * FROM categories")
    public abstract LiveData<List<CategoryEntity>> all();

//    @Query("SELECT * FROM categories WHERE parent_id LIKE :categoryId")
//    List<CategoryEntity> getSubCategories(int categoryId);
//
//    @Query("SELECT * FROM categories WHERE parent_id LIKE 0")
//    List<CategoryEntity>  getCategories();
}
