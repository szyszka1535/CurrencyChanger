package org.currencychanger;

import org.currencychanger.exchangerates.FetchRatesException;
import org.currencychanger.exchangerates.RateCalculator;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type how much GBP you want to exchange: ");
        String amount = scanner.nextLine();
        RateCalculator calculator = RateCalculator.create();

        try {
            var result = calculator.exchangeRate(amount);
            System.out.println("Your exchanged amount is: " + result);
        } catch (FetchRatesException e) {
            System.err.println("Caught you RedHanded: you put something wrong in my terminal, i needed some number");
            throw new RuntimeException(e);
        }
    }
}
