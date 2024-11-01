package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.IntegrationEnvironment;
import edu.java.kudagoapi.dtos.LocationDto;
import edu.java.kudagoapi.model.Location;
import edu.java.kudagoapi.repositories.JpaLocationRepository;
import edu.java.kudagoapi.services.location.JpaLocationService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest("app.database-access-type=jpa")
@AutoConfigureMockMvc
public class LocationControllerSystemTest extends IntegrationEnvironment {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JpaLocationRepository repo;
    @Autowired
    private JpaLocationService jpaLocationService;

    @BeforeEach
    @Transactional
    public void cleanUp() {
        repo.deleteAll();
    }

    @Test
    public void testGetAll() throws Exception {
        //given
        Location location1 = new Location("test1", "test1", "ru");
        Location location2 = new Location("test2", "test2", "ru");
        repo.saveAll(List.of(
                location1,
                location2
        ));

        //then
        mvc.perform(get("/api/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                  {
                                    "id": %d,
                                    "name": "test1",
                                    "slug": "test1",
                                    "language": "ru"
                                  },
                                  {
                                    "id": %d,
                                    "name": "test2",
                                    "slug": "test2",
                                    "language": "ru"
                                  }
                                ]
                                """.formatted(location1.getId(), location2.getId())
                ));

    }

    @Test
    public void testGetById() throws Exception {
        //given
        Location location = repo.save(new Location("test1", "test1", "ru"));

        //then
        mvc.perform(get("/api/v1/locations/{id}", location.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {
        //then
        mvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "test",
                                "slug": "test",
                                "language": "ru"
                                }
                                """))
                .andExpect(status().isOk());
        List<Location> all = repo.findAll();
        assertThat(all).size().isEqualTo(1);
        Location expectedResult = new Location(
                all.getFirst().getId(),
                "test",
                "test",
                "ru"
        );
        assertThat(all.getFirst()).isEqualTo(expectedResult);
    }

    @Test
    public void testCreateWhenAlreadyExists() throws Exception {
        //given
        repo.save(new Location(
                "test",
                "test",
                "ru"
        ));

        //then
        mvc.perform(post("/api/v1/locations")
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
                        "message": "Key (slug)=(test) already exists."
                        }
                        """));
    }

    @Test
    public void testPutUpdate() throws Exception {
        //given
        Location location = repo.save(new Location(
                "test1",
                "test1",
                "ru"
        ));

        //then
        mvc.perform(put("/api/v1/locations/{id}", location.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "test2",
                                "slug": "test2"
                                }
                                """))
                .andExpect(status().isOk());
        Location expectedResult = new Location(
                location.getId(),
                "test2",
                "test2",
                null
        );
        assertThat(repo.findById(location.getId()).get()).isEqualTo(expectedResult);
    }

    @Test
    public void testPutUpdateWhenNotFound() throws Exception {
        //then
        mvc.perform(put("/api/v1/locations/1")
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
    public void testDelete() throws Exception {
        //given
        Location location = repo.save(new Location("test1", "test1", "ru"));

        //then
        mvc.perform(delete("/api/v1/locations/{id}", location.getId()))
                .andExpect(status().isOk());
        assertThat(repo.findById(location.getId())).isNotPresent();
    }

    @Test
    public void testDeleteWhenNotFound() throws Exception {
        //then
        mvc.perform(delete("/api/v1/locations/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                        "code": 404,
                        "message": "Location with id 1 not found"
                        }
                        """));
    }

    @Test
    public void testUndoUpdate() throws Exception {
        //when
        Location location = repo.save(new Location("test1", "test1", "ru"));
        Long id = location.getId();
        jpaLocationService.fullUpdate(id, new LocationDto("test2", "test2", "ru"));
        mvc.perform(put("/api/v1/locations/undo/{id}", id))
                .andExpect(status().isOk());

        //then
        assertThat(repo.findById(id)).isPresent();
        assertThat(repo.findById(id).get()).isEqualTo(location);
    }

    @Test
    public void testUndoUpdateWhenNotFound() throws Exception {
        //then
        mvc.perform(put("/api/v1/locations/undo/{id}", 1000))
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                          "code": 404,
                          "message": "No snapshot found for location id 1000"
                        }
                        """));
    }
}
