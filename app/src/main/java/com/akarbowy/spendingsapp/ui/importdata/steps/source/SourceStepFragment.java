package com.akarbowy.spendingsapp.ui.importdata.steps.source;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.ui.importdata.ImportViewModel;
import com.akarbowy.spendingsapp.ui.importdata.steps.StepPage;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SourceStepFragment extends Fragment implements StepPage {

    private static final int FILE_CHOOSER_REQUEST_CODE = 23;

    @BindView(R.id.source_group) RadioGroup sourceGroup;

    private ImportViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.import_step_source_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {

        initViewModel();

        initSources();

        setOnSourceSelectedListener();
    }

    private void initSources() {
        sourceGroup.clearCheck();
    }

    private void setOnSourceSelectedListener() {
        sourceGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.source_revolut:
                    final RadioButton option = group.findViewById(checkedId);
                    if (option.isChecked()) {
                        onRevolutSourceSelected();
                    }
                    break;
            }
        });
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(getActivity()).get(ImportViewModel.class);

        viewModel.setCurrentStepPage(this);
    }

    private void onRevolutSourceSelected() {
        showFileChooser();
    }

    private void showFileChooser() {

        final Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");

        startActivityForResult(intent, FILE_CHOOSER_REQUEST_CODE);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_CANCELED) {
                initSources();
            } else if (resultCode == Activity.RESULT_OK) {

                if (data != null) {
                    viewModel.setSourceFileUri(data.getData());
                }
            }
        }
    }

    @NonNull @Override public Fragment getFragment() {
        return this;
    }

    @Override public String getPageTitle() {
        return getString(R.string.import_step_source_title);
    }

    @Override public int getPageOrdinal() {
        return 1;
    }

}
