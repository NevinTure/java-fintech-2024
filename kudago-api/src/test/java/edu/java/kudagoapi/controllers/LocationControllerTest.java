package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.IntegrationEnvironment;
import edu.java.kudagoapi.clients.KudagoClient;
import edu.java.kudagoapi.dtos.LocationDto;
import edu.java.kudagoapi.exceptions.BadRequestApiException;
import edu.java.kudagoapi.exceptions.LocationNotFoundApiException;
import edu.java.kudagoapi.services.LocationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LocationControllerTest extends IntegrationEnvironment {

    @MockBean
    LocationService service;
    @MockBean
    KudagoClient client;
    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGetAll() throws Exception {
        //then
        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().isOk());
        Mockito.verify(service, Mockito.times(1)).findAll();
    }

    @Test
    public void testGetById() throws Exception {
        //then
        mockMvc.perform(get("/api/v1/locations/1"))
                .andExpect(status().isOk());
        Mockito.verify(service, Mockito.times(1)).getById(1L);
    }

    @Test
    public void testGetByIdWhenNotFound() throws Exception {
        //when
        Mockito.when(service.getById(1L)).thenThrow(new LocationNotFoundApiException(1L));

        //then
        mockMvc.perform(get("/api/v1/locations/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                            "code": 404,
                            "message": "Location with id 1 not found"
                        }
                        """));
    }

    @Test
    public void testGetByIdWhenInvalidParams() throws Exception {
        //then
        mockMvc.perform(get("/api/v1/locations/0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "code": 400,
                        "message": "getById.id: must be greater than or equal to 1"
                        }
                        """));
    }

    @Test
    public void testCreate() throws Exception {
        //given
        LocationDto dto = new LocationDto(
                "test",
                "test",
                "ru"
        );

        //then
        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "test",
                                "slug": "test",
                                "language": "ru"
                                }
                                """))
                .andExpect(status().isOk());
        Mockito.verify(service, Mockito.times(1)).save(dto);
    }

    @Test
    public void testCreateWhenAlreadyExists() throws Exception {
        //given
        LocationDto dto = new LocationDto(
                "test",
                "test",
                "ru"
        );

        //when
        Mockito.when(service.save(dto))
                .thenThrow(new BadRequestApiException("Location with slug test already exists"));

        //then
        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "test",
                                "slug": "test",
                                "language": "ru"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "code": 400,
                        "message": "Location with slug test already exists"
                        }
                        """));
    }

    @Test
    public void testCreateWhenInvalidBody() throws Exception {
        //then
        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "test",
                                "slug": "",
                                "language": "ru"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "code": 400,
                        "message": "Invalid request params: slug size must be between 1 and 2147483647"
                        }
                        """));
    }

    @Test
    public void testPutUpdate() throws Exception {
        //given
        LocationDto dto = new LocationDto(
                "test",
                "test",
                "ru"
        );

        //then
        mockMvc.perform(put("/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "test",
                                "slug": "test",
                                "language": "ru"
                                }
                                """))
                .andExpect(status().isOk());
        Mockito.verify(service, Mockito.times(1)).fullUpdate(1L, dto);
    }

    @Test
    public void testPutUpdateWhenNotFound() throws Exception {
        //given
        LocationDto dto = new LocationDto(
                "test",
                "test",
                "ru"
        );

        //when
        Mockito.when(service.fullUpdate(1L, dto))
                .thenThrow(new LocationNotFoundApiException(1L));

        //then
        mockMvc.perform(put("/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "test",
                                "slug": "test",
                                "language": "ru"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                        "code": 404,
                        "message": "Location with id 1 not found"
                        }
                        """));
    }

    @Test
    public void testPutUpdateWhenInvalidParams() throws Exception {
        //then
        mockMvc.perform(put("/api/v1/locations/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "test",
                                "slug": "test",
                                "language": "ru"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "code": 400,
                        "message": "putUpdate.id: must be greater than or equal to 1"
                        }
                        """));
    }

    @Test
    public void testPutUpdateWhenInvalidBody() throws Exception {
        //then
        mockMvc.perform(put("/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "",
                                "slug": "",
                                "language": "ru"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "code": 400,
                        "message": "Invalid request params: slug size must be between 1 and 2147483647"
                        }
                        """));
    }

    @Test
    public void testDelete() throws Exception {
        //then
        mockMvc.perform(delete("/api/v1/locations/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteWhenNotFound() throws Exception {
        //when
        Mockito.when(service.deleteById(1L)).thenThrow(new LocationNotFoundApiException(1L));

        //then
        mockMvc.perform(delete("/api/v1/locations/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                        "code": 404,
                        "message": "Location with id 1 not found"
                        }
                        """));
    }

    @Test
    public void testDeleteWhenInvalidParams() throws Exception {
        //then
        mockMvc.perform(delete("/api/v1/locations/0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "code": 400,
                        "message": "delete.id: must be greater than or equal to 1"
                        }
                        """));
    }
}
