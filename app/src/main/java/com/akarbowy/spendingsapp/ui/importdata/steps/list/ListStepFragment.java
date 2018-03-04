package com.akarbowy.spendingsapp.ui.importdata.steps.list;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.entities.CategoryEntity;
import com.akarbowy.spendingsapp.ui.category.CategoryBottomSheet;
import com.akarbowy.spendingsapp.ui.importdata.ImportDataAdapter;
import com.akarbowy.spendingsapp.ui.importdata.ImportViewModel;
import com.akarbowy.spendingsapp.ui.importdata.steps.StepPage;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListStepFragment extends Fragment implements StepPage, CategoryBottomSheet.Callback {

    private static final int REQUEST_CODE_CATEGORY = 400;
    private static final String KEY_CATEGORY_FOR_ITEM = "KEY_CATEGORY_FOR_ITEM";

    @BindView(R.id.import_list) RecyclerView listView;

    private ImportViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.import_step_list_fragment, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        init();
    }

    private void init() {
        initViewModel();

        initImportedDataList();

        importData();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(getActivity()).get(ImportViewModel.class);
    }

    private void importData() {

        viewModel.setProgressVisibility(true);

        try {

            final InputStream input = getActivity().getContentResolver().openInputStream(viewModel.getSourceFileUri());

            viewModel.importData(input);

        } catch (FileNotFoundException e) {

            viewModel.setProgressVisibility(false);

            Toast.makeText(getActivity(), "Unable to open this file.", Toast.LENGTH_SHORT).show();
        }
    }

    private void initImportedDataList() {

        final ImportDataAdapter importDataAdapter = new ImportDataAdapter(getContext());

        importDataAdapter.setCallback(this::showCategories);

        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(importDataAdapter);

        viewModel.importedData.observe(this, data -> {
            importDataAdapter.refreshData(data);

            viewModel.setSaveVisibility(viewModel.isValid());
        });
    }

    private void showCategories(int forItemAtPosition) {
        final Bundle bundle = new Bundle();
        bundle.putInt(KEY_CATEGORY_FOR_ITEM, forItemAtPosition);

        CategoryBottomSheet categoryBottomSheet = CategoryBottomSheet.newInstance(bundle);
        categoryBottomSheet.setTargetFragment(this, REQUEST_CODE_CATEGORY);
        categoryBottomSheet.show(getActivity().getSupportFragmentManager(), "category");
    }

    @Override public void onCategoryChosen(Bundle bundle, CategoryEntity category) {
        viewModel.setCategoryForItem(bundle.getInt(KEY_CATEGORY_FOR_ITEM), category);
    }

    @NonNull @Override public Fragment getFragment() {
        return this;
    }

    @Override public String getPageTitle() {
        return getString(R.string.import_step_list_title);
    }

    @Override public int getPageOrdinal() {
        return 3;
    }
}
