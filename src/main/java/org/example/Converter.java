package org.example;

import java.util.regex.Pattern;

public class Converter {
    public static boolean isNeedToConvert(String operation, String number) {
        if (operation.equals("toDollars") & Pattern.matches("\\$[0-9.]+", number))
            return false;
        else if (operation.equals("toRubles") & Pattern.matches("[0-9.]+p", number)) {
            return false;
        } else return true;
    }

    public static boolean areNumbersInSameCurrency(String a, String b) {
        if (Pattern.matches("\\$[0-9.]+", a) & Pattern.matches("\\$[0-9.]+", b))
            return true;
        else if (Pattern.matches("[0-9.]+p", a) & Pattern.matches("[0-9.]+p", b))
            return true;
        else return false;
    }

    public static String getCurrency(String number) {
        return Pattern.matches("\\$[0-9.]+", number) ? "$" : "p";
    }

    public static double convert(String number, String currencyToConvert, double exchangeRate) {
        double r = Double.parseDouble(number.replace(getCurrency(number), ""));
        return currencyToConvert.equals("$") ? r / exchangeRate : r * exchangeRate;
    }


}
