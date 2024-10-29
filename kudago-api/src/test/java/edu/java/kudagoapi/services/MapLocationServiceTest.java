package edu.java.kudagoapi.services;

import edu.java.kudagoapi.IntegrationEnvironment;
import edu.java.kudagoapi.clients.KudagoClient;
import edu.java.kudagoapi.dtos.LocationDto;
import edu.java.kudagoapi.exceptions.BadRequestApiException;
import edu.java.kudagoapi.exceptions.LocationNotFoundApiException;
import edu.java.kudagoapi.model.Location;
import edu.java.kudagoapi.repositories.LocationRepository;
import edu.java.kudagoapi.services.location.LocationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest("app.database-access-type=map")
public class MapLocationServiceTest extends IntegrationEnvironment {

    @MockBean
    LocationRepository repository;
    @MockBean
    KudagoClient client;
    @Autowired
    LocationService service;
    @Autowired
    ModelMapper mapper;

    @Test
    public void testSaveAll() {
        //given
        LocationDto dto1 = new LocationDto(
                "test1",
                "test1",
                "ru"
        );
        LocationDto dto2 = new LocationDto(
                "test2",
                "test2",
                "ru"
        );
        Location location1 = mapper.map(dto1, Location.class);
        Location location2 = mapper.map(dto2, Location.class);

        //when
        service.saveAll(List.of(dto1, dto2));

        //then
        List<Location> expected = List.of(location1, location2);
        Mockito.verify(repository, Mockito.times(1)).saveAll(expected);
    }

    @Test
    public void testSave() {
        //given
        LocationDto dto = new LocationDto(
                "test",
                "test",
                "ru"
        );
        Location location = mapper.map(dto, Location.class);

        //when
        service.save(dto);

        //then
        Mockito.verify(repository, Mockito.times(1)).save(location);
    }

    @Test
    public void testSaveThenAlreadyExists() {
        //given
        Long id = 1L;
        Location location = new Location(
                "test",
                "test",
                "ru"
        );
        location.setId(id);
        LocationDto dto = new LocationDto(
                "test2",
                "test",
                "ru"
        );

        //when
        Mockito.when(repository.findBySlug("test")).thenReturn(Optional.of(location));

        //then
        assertThatExceptionOfType(BadRequestApiException.class)
                .isThrownBy(() -> service.save(dto));
    }

    @Test
    public void testGetById() {
        //given
        Long id = 1L;
        Location location = new Location(
                "test",
                "test",
                "ru"
        );
        location.setId(id);
        LocationDto dto = mapper.map(location, LocationDto.class);

        //when
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(location));

        //then
        assertThat(service.getById(id).getBody()).isEqualTo(dto);
    }

    @Test
    public void testGetByIdWhenNotFound() {
        //when
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(LocationNotFoundApiException.class)
                .isThrownBy(() -> service.getById(1L));
    }

    @Test
    public void testFindAll() {
        //given
        Location location1 = new Location(
                "test1",
                "test1",
                "ru"
        );
        location1.setId(1L);
        Location location2 = new Location(
                "test2",
                "test2",
                "ru"
        );
        location2.setId(2L);
        LocationDto dto1 = mapper.map(location1, LocationDto.class);
        LocationDto dto2 = mapper.map(location2, LocationDto.class);

        //when
        Mockito.when(repository.findAll()).thenReturn(List.of(location1, location2));

        //then
        List<LocationDto> expected = List.of(dto1, dto2);
        assertThat(service.findAll().getBody()).containsExactlyElementsOf(expected);
    }

    @Test
    public void testFullUpdate() {
        //given
        Long id = 1L;
        Location location = new Location(
                "test",
                "test",
                "ru"
        );
        location.setId(id);
        LocationDto dto = new LocationDto(
                "test2",
                "test2",
                "ru"
        );
        Location saved = mapper.map(dto, Location.class);
        saved.setId(id);

        //when
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(location));
        service.fullUpdate(id, dto);

        //then
        Mockito.verify(repository, Mockito.times(1)).save(saved);
    }

    @Test
    public void testFullUpdateWhenNotFound() {
        //given
        Long id = 1L;
        LocationDto dto = new LocationDto(
                "test2",
                "test2",
                "ru"
        );

        //when
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(LocationNotFoundApiException.class)
                .isThrownBy(() -> service.fullUpdate(id, dto));
    }

    @Test
    public void testDelete() {
        //given
        Long id = 1L;
        Location location = new Location(
                "test",
                "test",
                "ru"
        );
        location.setId(id);

        //when
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(location));
        service.deleteById(id);

        //then
        Mockito.verify(repository, Mockito.times(1)).deleteById(id);
    }

    @Test
    public void testDeleteWhenNotFound() {
        //when
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(LocationNotFoundApiException.class)
                .isThrownBy(() -> service.deleteById(1L));
    }
}
