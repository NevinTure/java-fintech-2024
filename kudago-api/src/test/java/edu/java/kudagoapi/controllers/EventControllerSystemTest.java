package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.IntegrationEnvironment;
import edu.java.kudagoapi.model.Event;
import edu.java.kudagoapi.model.Location;
import edu.java.kudagoapi.repositories.JpaEventRepository;
import edu.java.kudagoapi.repositories.JpaLocationRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class EventControllerSystemTest extends IntegrationEnvironment {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JpaEventRepository eventRepo;
    @Autowired
    private JpaLocationRepository locationRepo;

    @BeforeEach
    public void cleanUp() {
        eventRepo.deleteAll();
        locationRepo.deleteAll();
    }

    @Test
    public void testCreate() throws Exception {
        //given
        locationRepo.save(new Location(
                "test1",
                "test1",
                "ru"
        ));

        //then
        mvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "test1",
                                  "slug": "test1",
                                  "date": "2017-12-10",
                                  "location": {
                                    "slug": "test1"
                                  }
                                }
                                """))
                .andExpect(status().isOk());
        assertThat(eventRepo.findBySlug("test1")).isPresent();
    }

    @Test
    public void testCreateWhenInvalidBody() throws Exception {
        //then
        mvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "title": "test",
                  "date": "2010-10-10"
                }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWhenLocationNotFound() throws Exception {
        //then
        mvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "slug": "test",
                  "location": {
                    "slug": "test"
                  }
                }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetById() throws Exception {
        //given
        Location location = locationRepo.save(new Location("test1", "test1", "ru"));
        Event event = eventRepo
                .save(new Event("test1", "test1", LocalDate.of(2010, 10, 10), location));

        //then
        mvc.perform(get("/api/v1/events/{id}", event.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetByIdWhenInvalidParams() throws Exception {
        //then
        mvc.perform(get("/api/v1/events/{id}", 0))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetByIdWhenNotFound() throws Exception {
        //then
        mvc.perform(get("/api/v1/events/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAll() throws Exception {
        //given
        Location location = locationRepo.save(new Location("test1", "test1", "ru"));
        eventRepo
                .save(new Event("test1", "test1", LocalDate.of(2010,10,10), location));
        eventRepo
                .save(new Event("test2", "test2", LocalDate.of(2010,10,10), location));

        //then
        mvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllByFilter() throws Exception {
        //given
        Location location = locationRepo.save(new Location("test1", "test1", "ru"));
        eventRepo
                .save(new Event("test1", "test1", LocalDate.of(2010,10,10), location));
        eventRepo
                .save(new Event("test2", "test2", LocalDate.of(2010,10,10), location));
        eventRepo
                .save(new Event("test3", "test3", LocalDate.of(2010,8,10), location));

        //then
        mvc.perform(get("/api/v1/events/filter?fromDate=2010-10-10&title=test1"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                          [
                            {
                               "title": "test1",
                               "slug": "test1",
                               "date": "2010-10-10",
                               "location": {
                                  "id": %d,
                                  "name": "test1",
                                  "slug": "test1",
                                  "language": "ru"
                               }
                            }
                          ]
                          """.formatted(location.getId())
                ));
    }

    @Test
    public void testFullUpdate() throws Exception {
        //given
        Location location1 = locationRepo.save(new Location("test1", "test1", "ru"));
        Location location2 = locationRepo.save(new Location("test2", "test2", "ru"));
        Event event = eventRepo
                .save(new Event("test1", "test1", LocalDate.of(2010, 10, 10), location1));

        //then
        mvc.perform(put("/api/v1/events/{id}", event.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "slug": "test2",
                  "date": "2015-10-10",
                  "location": {
                    "slug": "test2"
                  }
                }
                """))
                .andExpect(status().isOk());
        Event expectedResult = new Event(event.getId(),null, "test2", LocalDate.of(2015, 10, 10), location2);
        assertThat(eventRepo.findById(event.getId())).isPresent();
        assertThat(eventRepo.findById(event.getId()).get()).isEqualTo(expectedResult);
    }

    @Test
    public void testFullUpdateWhenInvalidParams() throws Exception {
        //then
        mvc.perform(put("/api/v1/events/{id}", 0))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFullUpdateWhenNotFound() throws Exception {
        //then
        mvc.perform(put("/api/v1/events/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "slug": "test1",
                          "location": {
                            "slug": "test1"
                          }
                        }
                        """))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFullUpdateWhenInvalidBody() throws Exception {
        //then
        mvc.perform(put("/api/v1/events/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                          "title": "test",
                          "date": "2024-10-10"
                        }
                        """
                ))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFullUpdateWhenLocationNotFound() throws Exception {
        //given
        Location location = locationRepo.save(new Location("test1", "test1", "ru"));
        Event event = eventRepo
                .save(new Event("test1", "test1", LocalDate.of(2010, 10, 10), location));

        //then
        mvc.perform(put("/api/v1/events/{id}", event.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                 {
                  "slug": "test2",
                  "date": "2024-10-10",
                  "location": {
                    "slug": "test2"
                  }
                 }
                 """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        Location location = locationRepo.save(new Location("test1", "test1", "ru"));
        Event event = eventRepo
                .save(new Event("test1", "test1", LocalDate.of(2010, 10, 10), location));

        //then
        mvc.perform(delete("/api/v1/events/{id}", event.getId()))
                .andExpect(status().isOk());
        assertThat(eventRepo.findById(event.getId())).isNotPresent();
    }

    @Test
    public void testDeleteWhenInvalidParams() throws Exception {
        //then
        mvc.perform(delete("/api/v1/events/{id}", 0))
                .andExpect(status().isBadRequest());
    }
}
