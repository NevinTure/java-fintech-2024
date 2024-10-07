package edu.java.currencyapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CurrencyConvertRequest {
    @NotNull
    private String fromCurrency;
    @NotNull
    private String toCurrency;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Min(1)
    private BigDecimal amount;
}
