package edu.java.currencyapi.dtos;

import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.Data;
import java.util.List;

@Data
@JacksonXmlRootElement(localName = "ValCurs")
public class CbrCurrenciesResponse {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Valute")
    private List<CbrCurrency> currencies;
}
