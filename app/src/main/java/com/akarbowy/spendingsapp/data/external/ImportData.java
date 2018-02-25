package com.akarbowy.spendingsapp.data.external;


import com.akarbowy.spendingsapp.data.entities.TransactionEntity;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class ImportData {

    private String reference;

    private double value;

    private String date;

    private String category;

    private String currencyIsoCode;

    /**
     * set by user
     */
    private int chosenCategoryId;

    private String chosenCategoryName;

    public ImportData(String reference, double value, String date, String category, String currencyIsoCode) {
        this.reference = reference;
        this.value = value;
        this.date = date;
        this.category = category;
        this.currencyIsoCode = currencyIsoCode;
    }

    public String getReference() {
        return reference;
    }

    public double getValue() {
        return value;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    public boolean isCategoryChosen() {
        return chosenCategoryId > 0;
    }

    public int getChosenCategoryId() {
        return chosenCategoryId;
    }

    public String getChosenCategoryName() {
        return chosenCategoryName;
    }

    public void setChosenCategory(int categoryId, String chosenCategoryName) {
        this.chosenCategoryId = categoryId;
        this.chosenCategoryName = chosenCategoryName;
    }

    public static class Mapper {

        public static List<ImportData> fromSource(List<? extends ImportSource> source) {
            final List<ImportData> data = new ArrayList<>();

            for (ImportSource s : source) {
                data.add(s.asImportData());
            }

            return data;
        }

        public static List<TransactionEntity> toTransactions(List<ImportData> data) {
            final List<TransactionEntity> transactions = new ArrayList<>();

            for (ImportData d : data) {
                transactions.add(toTransaction(d));
            }

            return transactions;
        }

        private static TransactionEntity toTransaction(ImportData metaData) {
            final TransactionEntity transaction = new TransactionEntity();

            transaction.title = metaData.getReference();
            transaction.value = metaData.getValue();

            final LocalTime localTime = ZonedDateTime.now(ZoneOffset.UTC).with(LocalTime.MIDNIGHT).toLocalTime();
            final LocalDate localDate = LocalDate.parse(metaData.getDate(), DateTimeFormatter.ofPattern("d MMM uuuu"));
            transaction.date = LocalDateTime.of(localDate, localTime);

            transaction.currencyId = metaData.getCurrencyIsoCode();
            transaction.categoryId = metaData.getChosenCategoryId();

            return transaction;
        }

    }
}
