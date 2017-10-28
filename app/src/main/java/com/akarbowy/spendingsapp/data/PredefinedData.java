package com.akarbowy.spendingsapp.data;

import com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup;
import com.akarbowy.spendingsapp.data.Dictionaries.Currency;
import com.akarbowy.spendingsapp.data.entities.CategoryEntity;
import com.akarbowy.spendingsapp.data.entities.CategoryGroupEntity;
import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;
import com.akarbowy.spendingsapp.data.entities.GroupedCategories;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by arek.karbowy on 26/10/2017.
 */

public class PredefinedData {


    public PredefinedData() {
    }


    public static List<GroupedCategories> getCategories() {
        List<GroupedCategories> list = new ArrayList<>();

        for (CategoryGroup group : EnumSet.allOf(CategoryGroup.class)) {

            List<CategoryEntity> categories = new ArrayList<>();
            for (CategoryGroup.Category category : group.categories) {
                categories.add(new CategoryEntity(category.title));
            }

            GroupedCategories groupedCategories = new GroupedCategories();
            groupedCategories.group = new CategoryGroupEntity(group.title);
            groupedCategories.categories = categories;

            list.add(groupedCategories);
        }

        return list;
    }

    public static List<CurrencyEntity> getCurrencies() {
        List<CurrencyEntity> list = new ArrayList<>();

        for (Currency currency : EnumSet.allOf(Currency.class)) {
            list.add(new CurrencyEntity(currency.symbol, currency.isoCode, currency.title));
        }

        return list;
    }

}