package org.example;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

public class Validator {
    public static boolean isInputCorrect(String input) {
        return areAcceptableSubstrings(input) & isNumberOfBracketsCorrect(input) & isTheEndCorrect(input);
    }

    private static boolean isNumberOfBracketsCorrect(String input) {
        int counter1 = StringUtils.countMatches(input, "(");
        int counter2 = StringUtils.countMatches(input, ")");
        return counter1 == counter2;
    }

    private static boolean areAcceptableSubstrings(String input) {
        return Pattern.compile("^(toDollars|toRubles|\\$\\d+((\\.|,)(\\d)+)*|\\d+((\\.|,)(\\d+))*p|[ +-]|\\(|\\)|\\$|p)*$").matcher(input).find();
    }

    private static boolean isTheEndCorrect(String string) {
        return Pattern.compile("([0-9 p.)])$").matcher(string).find();
    }
}
