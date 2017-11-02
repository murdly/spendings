package com.akarbowy.spendingsapp.ui;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;

import static com.akarbowy.spendingsapp.ui.SpendingsPeriod.Type.ALL_TIME;
import static com.akarbowy.spendingsapp.ui.SpendingsPeriod.Type.PREVIOUS_MONTH;
import static com.akarbowy.spendingsapp.ui.SpendingsPeriod.Type.THIS_MONTH;
import static org.threeten.bp.temporal.TemporalAdjusters.firstDayOfMonth;
import static org.threeten.bp.temporal.TemporalAdjusters.lastDayOfMonth;

/**
 * Created by arek.karbowy on 31/10/2017.
 */
public class SpendingsPeriod {

    public enum Type {
        THIS_MONTH("This month"),
        PREVIOUS_MONTH("Previous month"),
        ALL_TIME("All time"),
        CUSTOM("Custom");

        public final String title;

        Type(String title) {
            this.title = title;
        }

    }

    private final LocalDateTime from;
    private final LocalDateTime to;

    private SpendingsPeriod(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public static SpendingsPeriod custom(LocalDateTime from, LocalDateTime to) {
        return new SpendingsPeriod(from, to);
    }

    public static SpendingsPeriod of(Type type) {
        LocalDateTime startOfToday = LocalDate.now(ZoneOffset.UTC).atStartOfDay();
        LocalDateTime from;
        LocalDateTime to;
        if (type == THIS_MONTH) {
            from = startOfToday.with(firstDayOfMonth());
            to = from.plusMonths(1).minusNanos(1);
            return new SpendingsPeriod(from, to);
        } else if (type == PREVIOUS_MONTH) {
            from = startOfToday.with(firstDayOfMonth()).minusMonths(1);
            to = startOfToday.with(firstDayOfMonth()).minusNanos(1);
            return new SpendingsPeriod(from, to);
        } else if (type == ALL_TIME) {
            from = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
            to = LocalDateTime.now(ZoneOffset.UTC);
            return new SpendingsPeriod(from, to);
        } else {
            return new SpendingsPeriod(null, null);
        }
    }

    public LocalDateTime from() {
        return from;
    }

    public LocalDateTime to() {
        return to;
    }
}
