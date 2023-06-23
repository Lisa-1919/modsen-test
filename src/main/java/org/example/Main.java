package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String stringToCalculate = "";
        stringToCalculate = scanner.nextLine();
        while (!stringToCalculate.equals("exit")) {
            System.out.println(Calculator.calculate(stringToCalculate));
            stringToCalculate = scanner.nextLine();
        }
    }
}