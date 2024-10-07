package edu.java.currencyapi.services;

import edu.java.currencyapi.clients.CbrClient;
import edu.java.currencyapi.dtos.*;
import edu.java.currencyapi.exceptions.BadRequestApiException;
import edu.java.currencyapi.exceptions.NotFoundApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CbrClient client;
    private final Set<Currency> buildInCurrencies = Currency.getAvailableCurrencies();
    private static final CbrCurrency RUBLE
            = new CbrCurrency(643, "RUB", new BigDecimal(1));

    @Override
    public ResponseEntity<CurrencyRateResponse> getRate(Integer code) {
        Optional<CbrCurrency> cbrCurrencyOp = findCbrCurrency(v -> v.getCode().equals(code));
        Optional<Currency> currencyOp = findBuildInCurrency(v -> v.getNumericCode() == code);
        if (currencyOp.isPresent() && cbrCurrencyOp.isPresent()) {
            CbrCurrency currency = cbrCurrencyOp.get();
            return ResponseEntity.ok(
                    new CurrencyRateResponse(currency.getCharCode(), currency.getValue()));
        }
        if (currencyOp.isEmpty()) {
            throw new BadRequestApiException(String.format("Currency with code %d not found in CB api", code));
        }
        throw new NotFoundApiException(String.format("Currency with code %d not found", code));
    }

    @Override
    public ResponseEntity<CurrencyConvertResponse> convert(CurrencyConvertRequest request) {
        BigDecimal amount = request.getAmount();
        BigDecimal from = getRateByStrCode(request.getFromCurrency());
        BigDecimal to = getRateByStrCode(request.getToCurrency());
        BigDecimal ans = from
                .setScale(4, RoundingMode.HALF_UP)
                .divide(to, RoundingMode.HALF_UP)
                .multiply(amount);
        return ResponseEntity
                .ok(new CurrencyConvertResponse(
                        request.getFromCurrency(),
                        request.getToCurrency(),
                        ans));
    }

    private Optional<CbrCurrency> findCbrCurrency(Predicate<CbrCurrency> filter) {
        if (filter.test(RUBLE)) {
            return Optional.of(RUBLE);
        }
        return client
                .getCurrencies()
                .getCurrencies()
                .stream()
                .filter(filter)
                .findFirst();
    }

    private Optional<Currency> findBuildInCurrency(Predicate<Currency> filter) {
        return buildInCurrencies
                .stream()
                .filter(filter)
                .findFirst();
    }

    private BigDecimal getRateByStrCode(String strCode) {
        Optional<CbrCurrency> cbrCurrencyOp = findCbrCurrency(v -> v.getCharCode().equals(strCode));
        Optional<Currency> currencyOp = findBuildInCurrency(v -> v.getCurrencyCode().equals(strCode));
        if (currencyOp.isPresent() && cbrCurrencyOp.isPresent()) {
            return cbrCurrencyOp.get().getValue();
        }
        if (currencyOp.isEmpty()) {
            throw new BadRequestApiException(String.format("Currency with code %s not found", strCode));
        }
        throw new NotFoundApiException(String.format("Currency with code %s not found in CB api", strCode));
    }
}
