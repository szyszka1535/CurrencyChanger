package org.currencychanger.exchangerates;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

public class NBPCalculatorTest {
    private static String VALID_EXCHANGE_JSON = "{\"table\":\"A\",\"currency\":\"funt szterling\",\"code\":\"GBP\",\"rates\":[{\"no\":\"047/A/NBP/2023\",\"effectiveDate\":\"2023-03-08\",\"mid\":5.2811}]}";

    @Test
    void positiveScenario() throws FetchRatesException {
        NBPCalculator calculator = new NBPCalculator(new StubNBPApiClient(VALID_EXCHANGE_JSON));
        Assertions.assertEquals(
                "232,37 PLN",
                calculator.exchangeRate("44")
        );
    }

    @Test
    void invalidInput() {
        NBPCalculator calculator = new NBPCalculator(new StubNBPApiClient(VALID_EXCHANGE_JSON));
        Assertions.assertThrows(NumberFormatException.class, () -> calculator.exchangeRate("pies"));
    }

    @Test
    void iOExceptionError() {
        NBPCalculator calculator = new NBPCalculator(new DownloadErrorApiClient(new IOException()));
        Assertions.assertThrows(FetchRatesException.class, () -> calculator.exchangeRate("55"));
    }

    @Test
    void interruptedExceptionError() {
        NBPCalculator calculator = new NBPCalculator((new DownloadErrorApiClient(new InterruptedException())));
        Assertions.assertThrows(FetchRatesException.class, () -> calculator.exchangeRate("55"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "",
            "{\"rates\":[{}]}}",
            "{\"rates\":[]}}",
            "{\"rates\":[{\"no\":\"047/A/NBP/2023\",\"effectiveDate\":\"2023-03-08\",\"mi7d\":5.2811}]}}",
            "{\"rates\":[{\"no\":\"047/A/NBP/2023\",\"effectiveDate\":\"2023-03-08\",\"mid\":pies}]}}"})
    void errorJson(String json) {
        NBPCalculator calculator = new NBPCalculator(new StubNBPApiClient(json));
        Assertions.assertThrows(FetchRatesException.class, () -> calculator.exchangeRate("55"));
    }

    static class StubNBPApiClient implements NBPApiClient {

        private final String json;

        StubNBPApiClient(String json) {
            this.json = json;
        }

        @Override
        public String fetchExchangeRateFromNBP() {
            return json;
        }
    }

    static class DownloadErrorApiClient implements NBPApiClient {

        final Exception exception;

        DownloadErrorApiClient(IOException exception) {
            this.exception = exception;
        }

        DownloadErrorApiClient(InterruptedException exception) {
            this.exception = exception;
        }

        @Override
        public String fetchExchangeRateFromNBP() throws IOException, InterruptedException {
            if (exception instanceof IOException) {
                throw (IOException) exception;
            } else if (exception instanceof InterruptedException) {
                throw (InterruptedException) exception;
            }
            throw new IllegalArgumentException("Exception type %s is not supported", exception);
        }
    }
}
