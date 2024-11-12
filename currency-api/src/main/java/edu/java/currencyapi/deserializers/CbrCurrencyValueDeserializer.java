package edu.java.currencyapi.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.math.BigDecimal;

public class CbrCurrencyValueDeserializer extends JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonParser parser,
                                  DeserializationContext deserializationContext) throws IOException {
        String valStr = parser.getText();
        if (valStr.contains(",")) {
            valStr = valStr.replace(",", ".");
        }
        return new BigDecimal(valStr);
    }
}
