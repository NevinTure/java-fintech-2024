package edu.java.kudagoapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
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