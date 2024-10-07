package edu.java.currencyapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CurrencyConvertRequest {
    private String fromCurrency;
    private String toCurrency;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Min(1)
    private BigDecimal amount;
}
