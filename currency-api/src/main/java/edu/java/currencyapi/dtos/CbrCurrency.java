package edu.java.currencyapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.java.currencyapi.deserializers.CbrCurrencyValueDeserializer;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CbrCurrency {
    @JsonProperty("NumCode")
    private Integer code;
    @JsonProperty("VunitRate")
    @JsonDeserialize(using = CbrCurrencyValueDeserializer.class)
    private BigDecimal value;
}
