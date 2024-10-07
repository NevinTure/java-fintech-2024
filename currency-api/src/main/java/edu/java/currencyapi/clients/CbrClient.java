package edu.java.currencyapi.clients;

import edu.java.currencyapi.dtos.CbrCurrenciesResponse;

public interface CbrClient {

    CbrCurrenciesResponse getCurrencies();
}
