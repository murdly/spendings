package com.akarbowy.spendingsapp.ui.importdata;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.ui.importdata.steps.StepPage;
import com.akarbowy.spendingsapp.ui.transaction.TransactionRepository;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImportDataActivity extends AppCompatActivity {

    private static final String BACK_STACK_NAME = "steps";

    @BindView(R.id.steps_count) TextView stepsCountView;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.save) Button saveButton;
    @BindView(R.id.progress) ProgressBar progressView;

    private ImportViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_data);
        ButterKnife.bind(this);

        init();
    }

    private void init() {

        initViewModel();

        initSteps();
    }

    private void initViewModel() {

        final TransactionRepository transactionRepository =
                new TransactionRepository(AppDatabase.getInstance(this));

        viewModel = ViewModelProviders.of(this,
                new ImportViewModel.Factory(transactionRepository)).get(ImportViewModel.class);
    }

    private void initSteps() {

        getSupportFragmentManager().popBackStack(BACK_STACK_NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        viewModel.currentStepPage.observe(this, page -> setStepFragment(page.getFragment()));

        viewModel.isProgressVisible.observe(this, visible -> progressView.setVisibility(visible ? View.VISIBLE : View.GONE));

        viewModel.isSaveVisible.observe(this, visible -> saveButton.setVisibility(visible ? View.VISIBLE : View.GONE));
    }

    private void setStepFragment(Fragment fragment) {

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.steps, fragment);
        transaction.addToBackStack(BACK_STACK_NAME);
        transaction.commit();

        getSupportFragmentManager().addOnBackStackChangedListener(() -> bindSelectedStep(viewModel.currentStepPage.getValue()));
    }

    private void bindSelectedStep(StepPage page) {
        title.setText(page.getPageTitle());

        final String text = getString(R.string.import_button_next,
                page.getPageOrdinal(), ImportViewModel.STEPS_COUNT);
        stepsCountView.setText(text);
    }

    @Override public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.save)
    public void onSaveButtonClicked() {
        viewModel.saveData();

        Toast.makeText(this, "Imported data successfully saved.", Toast.LENGTH_SHORT).show();

        finish();
    }


}
