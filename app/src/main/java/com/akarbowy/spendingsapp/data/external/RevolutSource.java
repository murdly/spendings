package com.akarbowy.spendingsapp.data.external;

import android.support.annotation.NonNull;

import com.akarbowy.spendingsapp.ui.importdata.ImportSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Csv pattern:
 * Completed Date ;
 * Reference ;
 * Paid Out (GBP) ;
 * Paid In (GBP) ;
 * Exchange Out ;
 * Exchange In ;
 * Balance (GBP) ;
 * Category
 * <p>
 * Categories:
 * shopping, transport, services, restaurants, travel,
 * entertainment, health, utilities,
 * cash, transfers, general
 */
public class RevolutSource implements ImportSource {

    private String currencyCode;

    private String completedDate;

    private String reference;

    private double paidOut;

    private double paidIn;

    private double exchangeOut;

    private double exchangeIn;

    private double balance;

    private String category;

    private RevolutSource(String currencyCode, String[] data) {
        this.currencyCode = currencyCode;
        this.completedDate = data[0];
        this.reference = data[1];
        this.paidOut = data[2].isEmpty() ? 0d : Double.valueOf(data[2]);
        this.paidIn = data[3].isEmpty() ? 0d : Double.valueOf(data[3]);
        this.exchangeOut = data[4].isEmpty() ? 0d : Double.valueOf(data[4]);
        this.exchangeIn = data[5].isEmpty() ? 0d : Double.valueOf(data[5]);
        this.balance = data[6].isEmpty() ? 0d : Double.valueOf(data[6]);
        this.category = data[7];
    }

    private static RevolutSource createRevolutSource(String currencyCode, String[] data) {
        return new RevolutSource(currencyCode, data);
    }

    private static List<RevolutSource> create(String currencyCode, List<String> entries) {
        //first line consists entry pattern
        entries.remove(0);

        final List<RevolutSource> data = new ArrayList<>();

        for (int i = 0; i < entries.size(); i++) {
            final String[] entry = entries.get(i).split(";");

            for (int k = 0; k < entry.length; k++) {
                entry[k] = entry[k].trim().replaceAll("\"", "");

            }

            data.add(createRevolutSource(currencyCode, entry));
        }

        return data;
    }

    public static List<RevolutSource> create(String currencyCode, List<String> entries, boolean filterExpenses) {
        final List<RevolutSource> all = create(currencyCode, entries);

        return filterExpenses ? filterExpenses(all) : all;
    }

    @NonNull private static List<RevolutSource> filterExpenses(List<RevolutSource> entries) {
        final List<RevolutSource> filtered = new ArrayList<>();

        for (RevolutSource entry : entries) {
            if (entry.paidOut != 0) {
                filtered.add(entry);
            }
        }
        return filtered;
    }

    @Override public ImportData asImportData() {
        return new ImportData(reference, paidOut, completedDate, category, currencyCode);
    }

}
