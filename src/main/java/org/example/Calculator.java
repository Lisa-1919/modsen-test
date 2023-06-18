package org.example;

import org.apache.commons.lang.StringUtils;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class Calculator {
    public static String calculate(String stringToCalculate, double exchangeRate) {
        stringToCalculate = preprocessInput(stringToCalculate);
        LinkedList<String> operations = new LinkedList<>();
        LinkedList<Double> numbers = new LinkedList<>();
        parse(stringToCalculate, numbers, operations);
        while (operations.size() != 0) {
            operate(numbers, operations.removeLast());
        }
        return String.valueOf(numbers.removeLast());
    }

    private static void operate(LinkedList<Double> numbers, String operator) {
        double r = numbers.removeLast();
        switch (operator) {
            case "+" -> numbers.add(r + numbers.removeLast());
            case "-" -> numbers.add(numbers.removeLast() - r);
            case "toDollars" -> numbers.add(r / 60);
            case "toRubles" -> numbers.add(r * 60);
        }
    }

    private static void parse(String stringToCalculate, LinkedList<Double> numbers, LinkedList<String> operations) {
        StringBuilder tempFunction = new StringBuilder();
        StringBuilder tempNumber = new StringBuilder();
        for (int i = 0; i < stringToCalculate.length(); i++) {
            if (stringToCalculate.charAt(i) == 't') {
                while (i < stringToCalculate.length() & !Pattern.matches("[0-9(]", String.valueOf(stringToCalculate.charAt(i)))) {
                    tempFunction.append(stringToCalculate.charAt(i));
                    i++;
                }
                i--;
                operations.add(tempFunction.toString());
                tempFunction = new StringBuilder();
            }
            if (Pattern.matches("[0-9]", String.valueOf(stringToCalculate.charAt(i)))) {
                while (i < stringToCalculate.length() && Pattern.matches("[0-9.]", String.valueOf(stringToCalculate.charAt(i)))) {
                    tempNumber.append(stringToCalculate.charAt(i));
                    i++;
                }
                i--;
                numbers.add(Double.parseDouble(tempNumber.toString()));
                tempNumber = new StringBuilder();
            }
            if (stringToCalculate.charAt(i) == '+' || stringToCalculate.charAt(i) == '-' || stringToCalculate.charAt(i) == '(') {
                operations.add(String.valueOf(stringToCalculate.charAt(i)));
            }
            if(stringToCalculate.charAt(i) == ')'){
                while (!operations.getLast().equals("("))
                    operate(numbers,operations.removeLast());
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
