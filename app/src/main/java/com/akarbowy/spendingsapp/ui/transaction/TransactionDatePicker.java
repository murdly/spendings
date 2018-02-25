package com.akarbowy.spendingsapp.ui.transaction;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;

public class TransactionDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private TransactionViewModel vm;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LocalDate last = vm.getLocalDateTime().toLocalDate();

        final DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, last.getYear(), last.getMonthValue(), last.getDayOfMonth());
        final DatePicker datePicker = dialog.getDatePicker();

        datePicker.setMaxDate(System.currentTimeMillis());
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = ViewModelProviders.of(getActivity()).get(TransactionViewModel.class);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        final LocalTime localTime = ZonedDateTime.now(ZoneOffset.UTC).with(LocalTime.MIDNIGHT).toLocalTime();
        vm.setLocalDateTime(LocalDateTime.of(LocalDate.of(year, month + 1, day), localTime));
    }
}
