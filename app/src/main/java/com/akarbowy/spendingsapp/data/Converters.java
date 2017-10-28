package com.akarbowy.spendingsapp.data;


import android.arch.persistence.room.TypeConverter;

import org.threeten.bp.LocalDate;


public class Converters {

    @TypeConverter
    public static LocalDate fromTimestamp(Long value) {
        return value == null ? null : LocalDate.ofEpochDay(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(LocalDate date) {
        return date == null ? null : date.toEpochDay();
    }
}
