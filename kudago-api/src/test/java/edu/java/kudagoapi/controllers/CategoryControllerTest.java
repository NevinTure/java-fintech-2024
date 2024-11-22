package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.IntegrationEnvironment;
import edu.java.kudagoapi.clients.KudagoClient;
import edu.java.kudagoapi.dtos.CategoryDto;
import edu.java.kudagoapi.exceptions.*;
import edu.java.kudagoapi.services.category.UpdatableCategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("without-security")
public class CategoryControllerTest extends IntegrationEnvironment {

    @MockBean
    UpdatableCategoryService service;
    @MockBean
    KudagoClient client;
    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGetAll() throws Exception {
        //then
        mockMvc.perform(get("/api/v1/places/categories"))
                .andExpect(status().isOk());
        Mockito.verify(service, Mockito.times(1)).findAll();
    }

    @Test
    public void testGetById() throws Exception {
        //then
        mockMvc.perform(get("/api/v1/places/categories/1"))
                .andExpect(status().isOk());
        Mockito.verify(service, Mockito.times(1)).getById(1);
    }

    @Test
    public void testGetByIdWhenNotFound() throws Exception {
        //when
        Mockito.when(service.getById(1)).thenThrow(new CategoryNotFoundApiException(1));

        //then
        mockMvc.perform(get("/api/v1/places/categories/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                            "code": 404,
                            "message": "Category with id 1 not found"
                        }
                        """));
    }

    @Test
    public void testGetByIdWhenInvalidParams() throws Exception {
        //then
        mockMvc.perform(get("/api/v1/places/categories/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "code": 400,
                        "message": "getById.id: must be greater than or equal to 0"
                        }
                        """));
    }

    @Test
    public void testCreate() throws Exception {
        //given
        CategoryDto dto = new CategoryDto(
                "test",
                "test"
        );

        //then
        mockMvc.perform(post("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "test",
                                "slug": "test"
                                }
                                """))
                .andExpect(status().isOk());
        Mockito.verify(service, Mockito.times(1)).save(dto, 1);
    }

    @Test
    public void testCreateWhenAlreadyExists() throws Exception {
        //given
        CategoryDto dto = new CategoryDto(
                "test",
                "test"
        );

        //when
        Mockito.when(service.save(dto, 1))
                .thenThrow(new BadRequestApiException("Category with id 1 already exists"));

        //then
        mockMvc.perform(post("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "test",
                                "slug": "test"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "code": 400,
                        "message": "Category with id 1 already exists"
                        }
                        """));
    }

    @Test
    public void testCreateWhenInvalidRequestParams() throws Exception {
        //then
        mockMvc.perform(post("/api/v1/places/categories/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "test",
                                "slug": "test"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "code": 400,
                        "message": "create.id: must be greater than or equal to 0"
                        }
                        """));
    }

    @Test
    public void testCreateWhenInvalidBody() throws Exception {
        //then
        mockMvc.perform(post("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "",
                                "slug": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "code": 400,
                        "message": "Invalid request params: name size must be between 2 and 2147483647, slug size must be between 1 and 2147483647"
                        }
                        """));
    }

    @Test
    public void testPutUpdate() throws Exception {
        //given
        CategoryDto dto = new CategoryDto(
                "test",
                "test"
        );

        //then
        mockMvc.perform(put("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "test",
                                "slug": "test"
                                }
                                """))
                .andExpect(status().isOk());
        Mockito.verify(service, Mockito.times(1)).fullUpdate(dto, 1);
    }

    @Test
    public void testPutUpdateWhenNotFound() throws Exception {
        //given
        CategoryDto dto = new CategoryDto(
                "test",
                "test"
        );

        //when
        Mockito.when(service.fullUpdate(dto, 1)).thenThrow(new CategoryNotFoundApiException(1));

        //then
        mockMvc.perform(put("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "test",
                                "slug": "test"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                        "code": 404,
                        "message": "Category with id 1 not found"
                        }
                        """));
    }

    @Test
    public void testPutUpdateWhenInvalidParams() throws Exception {
        //then
        mockMvc.perform(put("/api/v1/places/categories/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "test",
                                "slug": "test"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "code": 400,
                        "message": "putUpdate.id: must be greater than or equal to 0"
                        }
                        """));
    }

    @Test
    public void testPutUpdateWhenInvalidBody() throws Exception {
        //then
        mockMvc.perform(put("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "",
                                "slug": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "code": 400,
                        "message": "Invalid request params: name size must be between 2 and 2147483647, slug size must be between 1 and 2147483647"
                        }
                        """));
    }

    @Test
    public void testDelete() throws Exception {
        //then
        mockMvc.perform(delete("/api/v1/places/categories/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteWhenNotFound() throws Exception {
        //when
        Mockito.when(service.deleteById(1)).thenThrow(new CategoryNotFoundApiException(1));

        //then
        mockMvc.perform(delete("/api/v1/places/categories/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                        "code": 404,
                        "message": "Category with id 1 not found"
                        }
                        """));
    }

    @Test
    public void testDeleteWhenInvalidParams() throws Exception {
        //then
        mockMvc.perform(delete("/api/v1/places/categories/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "code": 400,
                        "message": "delete.id: must be greater than or equal to 0"
                        }
                        """));
    }

    @Test
    public void testUndoUpdate() throws Exception {
        //then
        mockMvc.perform(put("/api/v1/places/categories/undo/1"))
                .andExpect(status().isOk());
        Mockito.verify(service, Mockito.times(1)).undoUpdate(1L);
    }

    @Test
    public void testUndoUpdateWhenNotFound() throws Exception {
        //when
        Mockito.when(service.undoUpdate(1L)).thenThrow(new SnapshotNotFoundApiException(
                "No snapshot found for category id 1"
        ));

        //then
        mockMvc.perform(put("/api/v1/places/categories/undo/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                          "code": 404,
                          "message": "No snapshot found for category id 1"
                        }
                        """));
    }

    @Test
    public void testUndoUpdateWhenInvalidParams() throws Exception {
        //then
        mockMvc.perform(put("/api/v1/places/categories/undo/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "code": 400,
                        "message": "undoUpdate.id: must be greater than or equal to 0"
                        }
                        """));
    }
}
