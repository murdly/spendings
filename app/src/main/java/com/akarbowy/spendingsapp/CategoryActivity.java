package com.akarbowy.spendingsapp;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.entities.CategoryEntity;
import com.akarbowy.spendingsapp.ui.OverviewViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity {

    @BindView(R.id.list) ListView listView;

    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);


        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);

        OverviewViewModel vm = ViewModelProviders.of(this,
                new OverviewViewModel.Factory(AppDatabase.getInstance(this)))
                .get(OverviewViewModel.class);
    }

    private void updateUI(List<String> categories) {
        arrayAdapter.addAll(categories);
        arrayAdapter.notifyDataSetChanged();
    }

    private List<String> mapToString(List<CategoryEntity> all) {
        List<String> list = new ArrayList<>();
        for (CategoryEntity entity : all) {
            list.add(entity.toString());
        }
        return list;
    }
}
