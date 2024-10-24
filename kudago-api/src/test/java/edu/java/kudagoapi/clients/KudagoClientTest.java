package edu.java.kudagoapi.clients;

import edu.java.kudagoapi.IntegrationEnvironment;
import edu.java.kudagoapi.dtos.CategoryDto;
import edu.java.kudagoapi.dtos.LocationDto;
import edu.java.kudagoapi.services.CategoryService;
import edu.java.kudagoapi.services.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class KudagoClientTest extends IntegrationEnvironment {

    @Autowired
    KudagoClient kudagoClient;
    @MockBean
    CategoryService categoryService;
    @MockBean
    LocationService locationService;

    @Test
    public void testGetCategories() {
        //when
        List<CategoryDto> result = kudagoClient.getCategories();

        //then
        List<CategoryDto> expectedCategories = List.of(
                new CategoryDto(1L, "test1", "test1"),
                new CategoryDto(2L, "test2", "test2"),
                new CategoryDto(3L, "test3", "test3")
        );
        assertThat(result).containsExactlyElementsOf(expectedCategories);
    }

    @Test
    public void testGetLocation() {
        //when
        List<LocationDto> result = kudagoClient.getLocations("name,slug,language");

        //then
        List<LocationDto> expectedCategories = List.of(
                new LocationDto("test1", "test1", "ru"),
                new LocationDto("test2", "test2", "ru"),
                new LocationDto("test3", "test3", "ru")
        );
        assertThat(result).containsExactlyElementsOf(expectedCategories);
    }
}
