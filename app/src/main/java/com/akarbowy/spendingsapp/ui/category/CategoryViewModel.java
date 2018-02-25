package com.akarbowy.spendingsapp.ui.category;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.entities.GroupedCategories;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    public final LiveData<List<GroupedCategories>> categoriesByGroup;

    private AppDatabase appDatabase;

    public CategoryViewModel(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;

        categoriesByGroup = appDatabase.categoryDao().allGrouped();
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final AppDatabase appDatabase;

        public Factory(AppDatabase appDatabase) {
            this.appDatabase = appDatabase;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(CategoryViewModel.class)) {
                return (T) new CategoryViewModel(appDatabase);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
