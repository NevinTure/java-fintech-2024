package edu.java.currencyapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.java.currencyapi.deserializers.CbrCurrencyValueDeserializer;
import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CbrCurrency {
    @JsonProperty("NumCode")
    private Integer code;
    @JsonProperty("CharCode")
    private String charCode;
    @JsonProperty("VunitRate")
    @JsonDeserialize(using = CbrCurrencyValueDeserializer.class)
    private BigDecimal value;
}
