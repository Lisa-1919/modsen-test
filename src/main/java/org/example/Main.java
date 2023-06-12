package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try(FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        double exchangeRate = Double.parseDouble(properties.getProperty("exchange-rate"));
        Scanner scanner = new Scanner(System.in);
        String stringToCalculate = "";
        stringToCalculate = scanner.nextLine();
        while(!stringToCalculate.equals("exit")){
     //       System.out.println(Calculator.isCorrect(stringToCalculate));
            System.out.println(Calculator.calculate(stringToCalculate, exchangeRate));
            stringToCalculate = scanner.nextLine();
        }
    }

}