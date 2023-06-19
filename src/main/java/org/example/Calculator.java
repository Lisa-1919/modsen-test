package org.example;

import org.apache.commons.lang.StringUtils;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class Calculator {
    public static String calculate(String stringToCalculate, double exchangeRate) {
        stringToCalculate = preprocessInput(stringToCalculate);
        LinkedList<String> operations = new LinkedList<>();
        LinkedList<String> numbers = new LinkedList<>();
        parse(stringToCalculate, numbers, operations);
        while (operations.size() != 0) {
            operate(numbers, operations.removeLast());
        }
        String result = numbers.removeLast();
        String currency = Converter.getCurrency(result);
        result = result.replace(currency, "");
        result = String.format("%.2f", Double.parseDouble(result));
        if(currency.equals("$"))
            result = currency + result;
        else
            result = result + "p";
        return result;
    }

    private static void operate(LinkedList<String> numbers, String operator) {
        String r = numbers.removeLast();
        switch (operator) {
            case "+" -> {
                String a = numbers.removeLast();
                if (Converter.areNumbersInSameCurrency(r, a)) {
                    if (Converter.getCurrency(r).equals("$")) {
                        r = r.replace("$", "");
                        a = a.replace("$", "");
                        r = "$" + (Double.parseDouble(r) + Double.parseDouble(a));
                        numbers.add(r);
                    } else {
                        r = r.replace("p", "");
                        a = a.replace("p", "");
                        r = Double.parseDouble(r) + Double.parseDouble(a) + "p";
                        numbers.add(r);
                    }
                } else {
                    double numberA = Converter.convert(a);
                    String currency = Converter.getCurrency(r);
                    r = r.replace(Converter.getCurrency(r), "");
                    r = String.valueOf((Double.parseDouble(r) + numberA));
                    if (currency.equals("$"))
                        r = "$" + r;
                    else r = r + "p";
                    numbers.add(r);
                }
            }
            case "-" -> {
                String a = numbers.removeLast();
                if (Converter.areNumbersInSameCurrency(r, a)) {
                    if (Converter.getCurrency(r).equals("$")) {
                        r = r.replace("$", "");
                        a = a.replace("$", "");
                        r = "$" + (Double.parseDouble(a) - Double.parseDouble(r));
                        numbers.add(r);
                    } else {
                        r = r.replace("p", "");
                        a = a.replace("p", "");
                        r = Double.parseDouble(a) - Double.parseDouble(r) + "p";
                        numbers.add(r);
                    }
                } else {
                    double numberA = Converter.convert(a);
                    String currency = Converter.getCurrency(r);
                    r = r.replace(Converter.getCurrency(r), "");
                    r = String.valueOf(numberA - (Double.parseDouble(r)));
                    if (currency.equals("$"))
                        r = "$" + r;
                    else r = r + "p";
                    numbers.add(r);
                }
            }
            case "toDollars" -> {
                if (Converter.isNeedToConvert(operator, r)) {
                    r = r.replace("p", "");
                    r = "$" + Double.parseDouble(r) / 60;
                    numbers.add(r);
                } else numbers.add(r);
            }
            case "toRubles" -> {
                if (Converter.isNeedToConvert(operator, r)) {
                    r = r.replace("$", "");
                    r = Double.parseDouble(r) * 60 + "p";
                    numbers.add(r);
                } else numbers.add(r);

            }
        }
    }

    private static void parse(String stringToCalculate, LinkedList<String> numbers, LinkedList<String> operations) {
        StringBuilder tempFunction = new StringBuilder();
        StringBuilder tempNumber = new StringBuilder();
        for (int i = 0; i < stringToCalculate.length(); i++) {
            if (stringToCalculate.charAt(i) == 't') {
                while (i < stringToCalculate.length() & !Pattern.matches("[0-9($]+", String.valueOf(stringToCalculate.charAt(i)))) {
                    tempFunction.append(stringToCalculate.charAt(i));
                    i++;
                }
                i--;
                operations.add(tempFunction.toString());
                tempFunction = new StringBuilder();
            }
            if (Pattern.matches("[0-9$p]", String.valueOf(stringToCalculate.charAt(i)))) {
                while (i < stringToCalculate.length() && Pattern.matches("[0-9.$p]", String.valueOf(stringToCalculate.charAt(i)))) {
                    tempNumber.append(stringToCalculate.charAt(i));
                    i++;
                }
                i--;
                numbers.add(tempNumber.toString());
                tempNumber = new StringBuilder();
            }
            if (stringToCalculate.charAt(i) == '+' || stringToCalculate.charAt(i) == '-' || stringToCalculate.charAt(i) == '(') {
                operations.add(String.valueOf(stringToCalculate.charAt(i)));
            }
            if (stringToCalculate.charAt(i) == ')') {
                while (!operations.getLast().equals("("))
                    operate(numbers, operations.removeLast());
                operations.removeLast();
            }
        }
    }

    public static boolean isCorrect(String input) {
        String currencyFunction = "[ +-]*((toDollars|toRubles)\\()[a-zA-Z0-9., $+-]*\\)[ +-]*[a-zA-Z0-9 $+-]*";
        return Pattern.matches(currencyFunction, input);
    }

    public static String preprocessInput(String stringToCalculate) {
        stringToCalculate = stringToCalculate.replaceAll(",", ".");
        stringToCalculate = stringToCalculate.replaceAll(" ", "");
        return stringToCalculate;
    }

    public static boolean isValidate(String stringToCalculate) {
        int counter1 = StringUtils.countMatches(stringToCalculate, "(");
        int counter2 = StringUtils.countMatches(stringToCalculate, ")");
        return counter1 == counter2;
    }
}
