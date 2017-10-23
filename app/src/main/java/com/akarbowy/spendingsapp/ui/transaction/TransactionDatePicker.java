package com.akarbowy.spendingsapp.ui.transaction;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.ui.OverviewViewModel;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private TransactionViewModel vm;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LocalDate now = vm.getLocalDate();

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        DatePicker datePicker = dialog.getDatePicker();

        datePicker.setMaxDate(System.currentTimeMillis());
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = ViewModelProviders.of(getActivity()).get(TransactionViewModel.class);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        vm.setLocalDate(LocalDate.of(year, month, day));
    }
}
