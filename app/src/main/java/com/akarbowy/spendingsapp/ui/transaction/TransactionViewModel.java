package com.akarbowy.spendingsapp.ui.transaction;

import android.arch.lifecycle.ViewModel;

import com.akarbowy.spendingsapp.data.CurrencyDictionary;


/**
 * Created by arek.karbowy on 20/10/2017.
 */

public class TransactionViewModel extends ViewModel {

    CurrencyDictionary[] getAvailableCurrencies() {
        return new CurrencyDictionary[]{
                CurrencyDictionary.GBP,
                CurrencyDictionary.USD,
                CurrencyDictionary.EUR
        };
    }
}
