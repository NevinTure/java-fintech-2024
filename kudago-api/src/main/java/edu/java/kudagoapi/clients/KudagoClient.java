package edu.java.kudagoapi.clients;

import edu.java.kudagoapi.dtos.CategoryDto;
import edu.java.kudagoapi.dtos.LocationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(name = "kudago", url = "${app.kudago-api-base-url}")
public interface KudagoClient {

    @GetMapping("/place-categories")
    List<CategoryDto> getCategories();

    @GetMapping("/locations")
    List<LocationDto> getLocations(@RequestParam("fields") String fields);
}
