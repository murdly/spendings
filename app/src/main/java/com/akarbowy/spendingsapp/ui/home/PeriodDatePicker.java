package com.akarbowy.spendingsapp.ui.home;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.akarbowy.spendingsapp.ui.OverviewViewModel;
import com.akarbowy.spendingsapp.ui.transaction.TransactionViewModel;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;

import java.util.Calendar;

public class PeriodDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private OverviewViewModel vm;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LocalDate last;
//TODO check dates
        long max;
        long min;
        if (getTag().equals("periodFrom")) {
            last = vm.getPeriod().from().toLocalDate();
            min = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).toInstant(ZoneOffset.UTC).toEpochMilli();
            max = vm.getPeriod().to().toInstant(ZonedDateTime.now(ZoneOffset.UTC).getOffset()).toEpochMilli();
        } else { //periodTo
            last = vm.getPeriod().to().toLocalDate();
            min = vm.getPeriod().from().toInstant(ZonedDateTime.now(ZoneOffset.UTC).getOffset()).toEpochMilli();
            max = System.currentTimeMillis();
        }

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, last.getYear(), last.getMonthValue(), last.getDayOfMonth());
        DatePicker datePicker = dialog.getDatePicker();

        datePicker.setMinDate(min);
        datePicker.setMaxDate(max);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = ViewModelProviders.of(getActivity()).get(OverviewViewModel.class);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        LocalTime localTime = ZonedDateTime.now(ZoneOffset.UTC).with(LocalTime.MIDNIGHT).toLocalTime();

        if (getTag().equals("periodFrom")) {
            vm.setCustomPeriod(LocalDateTime.of(LocalDate.of(year, month + 1, day), localTime), vm.getPeriod().to());
        } else { //periodTo
            vm.setCustomPeriod(vm.getPeriod().from(), LocalDateTime.of(LocalDate.of(year, month + 1, day), localTime));
        }
    }
}