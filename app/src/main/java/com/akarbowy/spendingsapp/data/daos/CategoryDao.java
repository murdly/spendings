package com.akarbowy.spendingsapp.data.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.akarbowy.spendingsapp.data.entities.CategoryEntity;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    void insert(CategoryEntity category);

    @Insert
    void insert(List<CategoryEntity> categories);

    @Query("SELECT * FROM categories")
    LiveData<List<CategoryEntity>> all();

    @Query("SELECT * FROM categories WHERE parent_id LIKE :categoryId")
    List<CategoryEntity> getSubCategories(int categoryId);

    @Query("SELECT * FROM categories WHERE parent_id LIKE 0")
    List<CategoryEntity>  getCategories();
}
