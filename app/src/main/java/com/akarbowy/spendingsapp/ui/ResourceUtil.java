package com.akarbowy.spendingsapp.ui;

import android.content.Context;

import com.akarbowy.spendingsapp.R;

/**
 * Created by arek.karbowy on 30/10/2017.
 */

public class ResourceUtil {


    public static int getCategoryIconId(Context context, String groupName, String categoryName) {
        groupName = groupName.replace("&", "").replaceAll("\\s", "");
        categoryName = categoryName.replace("&", "").replaceAll("\\s", "");
        String iconName = String.format("ic_category_%1$s_%2$s", groupName, categoryName).toLowerCase();
        int id = context.getResources().getIdentifier(iconName,
                "drawable", context.getPackageName());

        return id != 0 ? id : R.drawable.ic_category_undefined_other;
    }
}
