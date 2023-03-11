package org.currencychanger.exchangerates;

public class FetchRatesException extends Exception {

    FetchRatesException(String message) {
        super(message);
    }

    FetchRatesException(String message, Throwable cause) {
        super(message, cause);
    }
}
