package edu.java.kudagoapi.clients;

import edu.java.customaspect.annotations.SemaphoreRateLimiter;
import edu.java.kudagoapi.configuration.BaseClientConfig;
import edu.java.kudagoapi.dtos.CurrencyConvertRequest;
import edu.java.kudagoapi.dtos.CurrencyConvertResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "currency",
url = "${app.currency-api-base-url}", configuration = BaseClientConfig.class)
public interface CurrencyClient {

    @SemaphoreRateLimiter("currency")
    @PostMapping("/currencies/convert")
    CurrencyConvertResponse convert(@RequestBody CurrencyConvertRequest request);

}
