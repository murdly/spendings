package com.akarbowy.spendingsapp.data;


import android.arch.persistence.room.TypeConverter;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;


public class Converters {

    @TypeConverter
    public static LocalDateTime fromTimestamp(Long value) {
        return value == null ?
                null
                : LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC);
    }

    @TypeConverter
    public static Long dateToTimestamp(LocalDateTime date) {
        return date == null ?
                null
                : date.toInstant(ZonedDateTime.now(ZoneOffset.UTC).getOffset()).toEpochMilli();
    }
}
