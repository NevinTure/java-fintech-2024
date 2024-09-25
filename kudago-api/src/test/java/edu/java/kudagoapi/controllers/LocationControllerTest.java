package edu.java.kudagoapi.controllers;

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
public class LocationControllerTest {

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
        mockMvc.perform(get("/api/v1/locations/name"))
                .andExpect(status().isOk());
        Mockito.verify(service, Mockito.times(1)).getById("name");
    }

    @Test
    public void testGetByIdWhenNotFound() throws Exception {
        //when
        Mockito.when(service.getById("name")).thenThrow(new LocationNotFoundApiException("name"));

        //then
        mockMvc.perform(get("/api/v1/locations/name"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                            "code": 404,
                            "message": "Location with id name not found"
                        }
                        """));
    }

    @Test
    public void testGetByIdWhenInvalidParams() throws Exception {
        //then
        mockMvc.perform(get("/api/v1/locations/a"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "code": 400,
                        "message": "getById.id: size must be between 2 and 2147483647"
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
        mockMvc.perform(post("/api/v1/locations/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "test",
                                "slug": "test",
                                "language": "ru"
                                }
                                """))
                .andExpect(status().isOk());
        Mockito.verify(service, Mockito.times(1)).save(dto, "test");
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
        Mockito.when(service.save(dto, "test"))
                .thenThrow(new BadRequestApiException("Location with id test already exists"));

        //then
        mockMvc.perform(post("/api/v1/locations/test")
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
                        "message": "Location with id test already exists"
                        }
                        """));
    }

    @Test
    public void testCreateWhenInvalidRequestParams() throws Exception {
        //then
        mockMvc.perform(post("/api/v1/locations/a")
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
                        "message": "create.id: size must be between 2 and 2147483647"
                        }
                        """));
    }

    @Test
    public void testCreateWhenInvalidBody() throws Exception {
        //then
        mockMvc.perform(post("/api/v1/locations/test")
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
                        "message": "Invalid request params: slug"
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
        mockMvc.perform(put("/api/v1/locations/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "test",
                                "slug": "test",
                                "language": "ru"
                                }
                                """))
                .andExpect(status().isOk());
        Mockito.verify(service, Mockito.times(1)).fullUpdate(dto, "test");
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
        Mockito.when(service.fullUpdate(dto, "test")).thenThrow(new LocationNotFoundApiException("test"));

        //then
        mockMvc.perform(put("/api/v1/locations/test")
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
                        "message": "Location with id test not found"
                        }
                        """));
    }

    @Test
    public void testPutUpdateWhenInvalidParams() throws Exception {
        //then
        mockMvc.perform(put("/api/v1/locations/a")
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
                        "message": "putUpdate.id: size must be between 2 and 2147483647"
                        }
                        """));
    }

    @Test
    public void testPutUpdateWhenInvalidBody() throws Exception {
        //then
        mockMvc.perform(put("/api/v1/locations/test")
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
                        "message": "Invalid request params: name, slug"
                        }
                        """));
    }

    @Test
    public void testDelete() throws Exception {
        //then
        mockMvc.perform(delete("/api/v1/locations/test"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteWhenNotFound() throws Exception {
        //when
        Mockito.when(service.deleteById("test")).thenThrow(new LocationNotFoundApiException("test"));

        //then
        mockMvc.perform(delete("/api/v1/locations/test"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                        "code": 404,
                        "message": "Location with id test not found"
                        }
                        """));
    }

    @Test
    public void testDeleteWhenInvalidParams() throws Exception {
        //then
        mockMvc.perform(delete("/api/v1/locations/a"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "code": 400,
                        "message": "delete.id: size must be between 2 and 2147483647"
                        }
                        """));
    }
}
