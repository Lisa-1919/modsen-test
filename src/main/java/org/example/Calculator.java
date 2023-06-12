package org.example;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    public static String calculate(String stringToCalculate, double exchangeRate) {
        String result = "";
        stringToCalculate = preprocessInput(stringToCalculate);
        LinkedList<String> operations = new LinkedList<>();
        LinkedList<String> numbers = new LinkedList<>();
        String tempFunction = "";
        String tempNumber = "";
        stringToCalculate = preprocessInput(stringToCalculate);
        for (int i = 0; i < stringToCalculate.length(); i++) {
            if (stringToCalculate.charAt(i) == 't') {
                while (i<stringToCalculate.length() & !Pattern.matches("[0-9]", String.valueOf(stringToCalculate.charAt(i)))) {
                    tempFunction += stringToCalculate.charAt(i);
                    i++;
                }
                i--;
                operations.add(tempFunction);
                tempFunction = "";
            }
            if (Pattern.matches("[0-9]", String.valueOf(stringToCalculate.charAt(i)))) {
                while (i<stringToCalculate.length() && Pattern.matches("[0-9.]", String.valueOf(stringToCalculate.charAt(i)))) {
                    tempNumber += stringToCalculate.charAt(i);
                    i++;
                }
                i--;
                numbers.add(tempNumber);
                tempNumber = "";
            }
            if (stringToCalculate.charAt(i)=='+' || stringToCalculate.charAt(i) =='-') {
                operations.add(String.valueOf(stringToCalculate.charAt(i)));
            }
        }
        double r = Double.parseDouble(numbers.removeLast());
        while (numbers.size() + operations.size() != 0) {
            switch (operations.removeLast()) {
                case "+" -> {
                    r += Double.parseDouble(numbers.removeLast());
                }
                case "-" -> {
                    r = Double.parseDouble(numbers.removeLast()) - r;
                }
                case "toDollars" ->{
                    r = r/exchangeRate;
                }
                case "toRubles" ->{
                    r = r*exchangeRate;
                }
            }
        }
        result = String.valueOf(r);
        return result;
    }

    public static boolean isCorrect(String input) {
        String currencySymbol = "";
        String currencyFunction = "[ +-]*((toDollars|toRubles)\\()[a-zA-Z0-9., $+-]*\\)[ +-]*[a-zA-Z0-9 $+-]*";
        String brackets = "[a-z]*\\([a-zA-z0-9., $]*\\)";
        return Pattern.matches(brackets, input);
//        return Pattern.matches("([+ -]*(toDollars\\(|toRubles\\()(((\\$\\d+)|[+_ a-zA-Z()]|(\\d+p))*\\)))*", input);
    }

    public static String preprocessInput(String stringToCalculate) {
        stringToCalculate = stringToCalculate.replace(",", ".");
        stringToCalculate = stringToCalculate.replace(" ", "");
        stringToCalculate = stringToCalculate.replace("(", "");
        stringToCalculate = stringToCalculate.replace(")", "");
        return stringToCalculate;
    }

    public static boolean isValidate(String stringToCalculate) {
        int counter1 = StringUtils.countMatches(stringToCalculate, "(");
        int counter2 = StringUtils.countMatches(stringToCalculate, ")");
        return counter1 == counter2;
    }
}
