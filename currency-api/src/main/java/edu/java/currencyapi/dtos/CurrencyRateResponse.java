package edu.java.currencyapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CurrencyRateResponse {
    private String currency;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private BigDecimal rate;
}
