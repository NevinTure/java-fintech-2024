package edu.java.kudagoapi.clients;

import edu.java.kudagoapi.configuration.BaseClientConfig;
import edu.java.kudagoapi.dtos.CategoryDto;
import edu.java.kudagoapi.dtos.LocationDto;
import edu.java.kudagoapi.dtos.events.EventsRequest;
import edu.java.kudagoapi.dtos.events.EventsResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;

@FeignClient(name = "kudago",
        url = "${app.kudago-api-base-url}", configuration = BaseClientConfig.class)
public interface KudagoClient {

    @GetMapping("/place-categories")
    List<CategoryDto> getCategories();

    @GetMapping("/locations")
    List<LocationDto> getLocations(@RequestParam("fields") String fields);

    @GetMapping("/events")
    EventsResponse getEvents(@SpringQueryMap Map<String, String> request);
}
