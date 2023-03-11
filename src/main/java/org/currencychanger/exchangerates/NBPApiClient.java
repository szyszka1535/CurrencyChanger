package org.currencychanger.exchangerates;

import java.io.IOException;

interface NBPApiClient {
    String fetchExchangeRateFromNBP() throws IOException, InterruptedException;
}
