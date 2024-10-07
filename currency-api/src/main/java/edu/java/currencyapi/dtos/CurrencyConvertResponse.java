package edu.java.currencyapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CurrencyConvertResponse {
    private String fromCurrency;
    private String toCurrency;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private BigDecimal convertedAmount;
}
