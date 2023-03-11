package org.currencychanger.exchangerates;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

class NBPCalculator implements RateCalculator {

    private final NBPApiClient nbpApiClient;

    NBPCalculator() {
        nbpApiClient = new NBPApiClientImpl();
    }

    NBPCalculator(NBPApiClient nbpApiClient) {
        this.nbpApiClient = nbpApiClient;
    }

    @Override
    public String exchangeRate(String input) throws FetchRatesException, NumberFormatException {
        float x = Float.parseFloat(input);
        float y = getRate();
        float result = (x * y);
        return String.format("%.02f PLN", result);
    }

    private float getRate() throws FetchRatesException {
        try {
            String jsonString = nbpApiClient.fetchExchangeRateFromNBP();
            if (jsonString == null) {
                throw new FetchRatesException("Fetched response is null");
            }
            JSONObject obj = new JSONObject(jsonString);
            JSONArray arr = obj.getJSONArray("rates");

            if (arr.isEmpty()) {
                throw new FetchRatesException("Fetched response is null");
            }
            return arr.getJSONObject(0).getFloat("mid");

        } catch (IOException | InterruptedException | JSONException e) {
            System.err.println("Caught " + e.getClass().getName() + ": " + e.getMessage());
            throw new FetchRatesException(e.getMessage(), e);
        }
    }
}
