package org.example;

import org.apache.commons.lang.StringUtils;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class Calculator {
    public static String calculate(String stringToCalculate, double exchangeRate) throws CalculatorException {
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
        result = currency.equals("$") ? "$" + result : result + "p";
        return result;
    }

    private static void operate(LinkedList<String> numbers, String operator) {
        String b = numbers.removeLast();
        switch (operator) {
            case "+", "-" -> {
                String a = numbers.removeLast();
                String currency = Converter.getCurrency(b);
                if (Converter.areNumbersInSameCurrency(b, a)) {
                    b = b.replace(Converter.getCurrency(b), "");
                    a = a.replace(Converter.getCurrency(a), "");
                } else {
                    a = String.valueOf(Converter.convert(a, Converter.getCurrency(b)));
                    b = b.replace(currency, "");
                }
                if (operator.equals("+")) b = String.valueOf(Double.parseDouble(a) + Double.parseDouble(b));
                else b = String.valueOf(Double.parseDouble(a) - Double.parseDouble(b));
                b = currency.equals("$") ? "$" + b : b + "p";
                numbers.add(b);
            }
            case "toDollars" -> {
                if (Converter.isNeedToConvert(operator, b)) {
                    b = b.replace(Converter.getCurrency(b), "");
                    b = "$" + Double.parseDouble(b) / 60;
                }
                numbers.add(b);
            }
            case "toRubles" -> {
                if (Converter.isNeedToConvert(operator, b)) {
                    b = b.replace(Converter.getCurrency(b), "");
                    b = Double.parseDouble(b) * 60 + "p";
                }
                numbers.add(b);
            }
        }
    }

    private static void parse(String stringToCalculate, LinkedList<String> numbers, LinkedList<String> operations) throws CalculatorException {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < stringToCalculate.length(); i++) {
            if (stringToCalculate.charAt(i) == 't') {
                while (!isOperator(temp.toString())) {
                    temp.append(stringToCalculate.charAt(i));
                    i++;
                }
                i--;
                operations.add(temp.toString());
                temp = new StringBuilder();
            }
            if (Pattern.matches("[0-9$p]", String.valueOf(stringToCalculate.charAt(i)))) {
                while (i < stringToCalculate.length() && Pattern.matches("[0-9.$p]", String.valueOf(stringToCalculate.charAt(i)))) {
                    temp.append(stringToCalculate.charAt(i));
                    i++;
                }
                i--;
                numbers.add(temp.toString());
                temp = new StringBuilder();
            }
            if (isOperator(String.valueOf(stringToCalculate.charAt(i)))) {
                operations.add(String.valueOf(stringToCalculate.charAt(i)));
            }
            if (stringToCalculate.charAt(i) == ')') {
                while (!operations.getLast().equals("("))
                    operate(numbers, operations.removeLast());
                operations.removeLast();
                if (Pattern.matches("(toDollars|toRubles)", operations.getLast()))
                    operate(numbers, operations.removeLast());
            }
        }

    }

    public static boolean isOperator(String operator) {
        return Pattern.matches("(toDollars|toRubles|\\+|-|\\()", operator);
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
