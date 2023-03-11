package org.currencychanger.exchangerates;

public interface RateCalculator {

    static RateCalculator create() {
        return new NBPCalculator();
    }

    String exchangeRate(String input) throws FetchRatesException, NumberFormatException;
}
