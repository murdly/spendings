package com.akarbowy.spendingsapp;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by arek.karbowy on 20/10/2017.
 */

public class AmountFormatUtil {

    public static String format(double value) {
        int power;
        String suffix = " kmbt";
        String formattedNumber;

        NumberFormat formatter = new DecimalFormat("#,###.#");
        power = (int) StrictMath.log10(value);
        value = value / (Math.pow(10, (power / 3) * 3));
        formattedNumber = formatter.format(value);
        formattedNumber = formattedNumber + suffix.charAt(power / 3);
        return formattedNumber.length() > 4 ?
                formattedNumber.replaceAll("\\.[0-9]+", "")
                : formattedNumber;
    }
}
