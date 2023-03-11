package org.currencychanger.exchangerates;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class NBPApiClientImpl implements NBPApiClient {
    @Override
    public String fetchExchangeRateFromNBP() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://api.nbp.pl/api/exchangerates/rates/a/gbp/2023-03-08/"))
                .build();
        return client
                .send(request, HttpResponse.BodyHandlers.ofString())
                .body();
    }
}
