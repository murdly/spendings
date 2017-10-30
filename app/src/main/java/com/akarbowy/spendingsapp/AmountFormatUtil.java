package com.akarbowy.spendingsapp;

/**
 * Created by arek.karbowy on 20/10/2017.
 */

public class AmountFormatUtil {

    public static String format(double amount) {
        long a = (long) Math.ceil(amount);
        if (a < 10000) {
            return "" + a;
        }

        int exp = (int) (Math.log(a) / Math.log(1000));

        return String.format("%.0f%c",
                a / Math.pow(1000, exp),
                "kMGTPE".charAt(exp - 1));
    }
}
