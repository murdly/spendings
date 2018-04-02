package com.akarbowy.spendingsapp.network.exchangerate;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExchangeRateResponse {

    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("rates")
    @Expose
    private Rates rates;

    public String getBase() {
        return base;
    }

    public double getRate(String isoCode) {
        switch (isoCode) {
            case "GBP":
                return rates.GBP;
            case "USD":
                return rates.USD;
            case "EUR":
                return rates.EUR;
            case "PLN":
                return rates.PLN;
            default:
                return 1d;
        }
    }

    private class Rates {
        @SerializedName("GBP")
        @Expose
        double GBP;
        @SerializedName("USD")
        @Expose
        double USD;
        @SerializedName("EUR")
        @Expose
        double EUR;
        @SerializedName("PLN")
        @Expose
        double PLN;
    }
}
