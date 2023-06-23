package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.regex.Pattern;

public class Calculator {
    private static double exchangeRate;

    public static String calculate(String stringToCalculate) throws Exception {
        if (Validator.isInputCorrect(stringToCalculate)) {
            readProperties();
            stringToCalculate = preprocessInput(stringToCalculate);
            LinkedList<String> operations = new LinkedList<>();
            LinkedList<String> numbers = new LinkedList<>();
            parse(stringToCalculate, numbers, operations);
            while (operations.size() != 0) {
                operate(numbers, operations.removeLast());
            }
            return getResult(numbers.removeLast());
        } else return "Ошибка ввода";
    }

    private static void operate(LinkedList<String> numbers, String operator) throws Exception {
        try {
            String b = numbers.removeLast();
            switch (operator) {
                case "+", "-" -> {
                    String a = numbers.removeLast();
                    String currency = Converter.getCurrency(b);
                    if (Converter.areNumbersInSameCurrency(b, a)) {
                        b = b.replace(Converter.getCurrency(b), "");
                        a = a.replace(Converter.getCurrency(a), "");
                    } else {
                        a = String.valueOf(Converter.convert(a, Converter.getCurrency(b), exchangeRate));
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
                        b = "$" + Double.parseDouble(b) / exchangeRate;
                    }
                    numbers.add(b);
                }
                case "toRubles" -> {
                    if (Converter.isNeedToConvert(operator, b)) {
                        b = b.replace(Converter.getCurrency(b), "");
                        b = Double.parseDouble(b) * exchangeRate + "p";
                    }
                    numbers.add(b);
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка вычисления");
        }
    }

    private static void parse(String stringToCalculate, LinkedList<String> numbers, LinkedList<String> operations) throws Exception {
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
                while (i < stringToCalculate.length() && Pattern.matches("[0-9.$p,]", String.valueOf(stringToCalculate.charAt(i)))) {
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

    private static String preprocessInput(String stringToCalculate) {
        stringToCalculate = stringToCalculate.replaceAll(",", ".");
        stringToCalculate = stringToCalculate.replaceAll(" ", "");
        return stringToCalculate;
    }

    private static void readProperties() {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        exchangeRate = Double.parseDouble(properties.getProperty("exchange-rate"));
    }

    private static String getResult(String number) {
        String currency = Converter.getCurrency(number);
        number = number.replace(currency, "");
        number = String.format("%.2f", Double.parseDouble(number));
        number = currency.equals("$") ? "$" + number : number + "p";
        return number;
    }
}
