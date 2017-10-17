package com.akarbowy.spendingsapp.data;


import com.akarbowy.spendingsapp.R;

public enum CurrencyDictionary {
    PLN(1, "zł", "PLN", "Polish Złoty", R.drawable.ic_euro_symbol_black_24dp),
    GBP(2, "Ł", "GBP", "British Pound", R.drawable.ic_euro_symbol_black_24dp),
    EUR(3, "E", "EUR", "Euro", R.drawable.ic_euro_symbol_black_24dp),
    USD(4, "$", "USD", "American Dollars", R.drawable.ic_euro_symbol_black_24dp);

    int id;
    String symbol;
    String abr;
    String name;
    int iconResId;

    CurrencyDictionary(int id, String symbol, String abr, String name, int iconResId) {
        this.id = id;
        this.symbol = symbol;
        this.abr = abr;
        this.name = name;
        this.iconResId = iconResId;
    }
}
